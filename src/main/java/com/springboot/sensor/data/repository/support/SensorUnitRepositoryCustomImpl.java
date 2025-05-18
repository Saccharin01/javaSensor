package com.springboot.sensor.data.repository.support;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.sensor.data.dto.AggregatedDataDTO;
import com.springboot.sensor.data.dto.SensorDataDTO;
import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.entity.QSensorData;
import com.springboot.sensor.data.entity.QSensorUnit;
import com.springboot.sensor.data.entity.SensorData;
import com.springboot.sensor.data.entity.SensorUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.querydsl.core.types.dsl.BooleanExpression;

@Repository
@RequiredArgsConstructor
public class SensorUnitRepositoryCustomImpl implements SensorUnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

    private final QSensorData data = QSensorData.sensorData;
    private final QSensorUnit unit = QSensorUnit.sensorUnit;

    @Override
    public SensorUnit findOrCreateSensorUnit(String chipId, String name, String location) {
        QSensorUnit qSensorUnit = QSensorUnit.sensorUnit;

        SensorUnit unit = queryFactory
                .selectFrom(qSensorUnit)
                .where(qSensorUnit.chipId.eq(chipId))
                .fetchOne();

        if (unit == null) {
            unit = new SensorUnit();
            unit.setChipId(chipId);
            unit.setName(name);
            unit.setLocation(location);
            entityManager.persist(unit); // JPA 직접 사용
        }

        return unit;
    }

    @Override
    public void saveSensorData(SensorUnit unit, int sensedData, LocalDateTime sensedTime) {
        SensorData data = new SensorData();
        data.setSensorUnit(unit);
        data.setSensedData(sensedData);
        data.setSensedTime(sensedTime);
        entityManager.persist(data);
    }

    @Override
    public List<String> getSensorUnitIds() {
        QSensorUnit qSensorUnit = QSensorUnit.sensorUnit;

        return queryFactory
                .select(qSensorUnit.chipId)
                .from(qSensorUnit)
                .fetch();
    }

    @Override
    public SensorResponseDTO findSensorUnitWithDataByChipId(String chipId) {
        QSensorUnit sensorUnit = QSensorUnit.sensorUnit;
        QSensorData sensorData = QSensorData.sensorData;

        var unit = queryFactory
                .selectFrom(sensorUnit)
                .leftJoin(sensorUnit.dataList, sensorData).fetchJoin()
                .where(sensorUnit.chipId.eq(chipId))
                .fetchOne();

        if (unit == null) {
            throw new IllegalArgumentException("SensorUnit with chipId " + chipId + " not found");
        }

        List<SensorDataDTO> dataList = unit.getDataList().stream()
                .map(d -> new SensorDataDTO(d.getSensedData(), d.getSensedTime()))
                .collect(java.util.stream.Collectors.toList());

        return new SensorResponseDTO(
                unit.getChipId(),
                unit.getName(),
                unit.getLocation(),
                unit.getSensorType(), 
                dataList
        );
    }

    @Override
    public SensorResponseDTO findSensorDataLimit(String chipId, int limit) {
        QSensorUnit sensorUnit = QSensorUnit.sensorUnit;
        QSensorData sensorData = QSensorData.sensorData;

        // 유닛 단독 조회
        SensorUnit unit = queryFactory
                .selectFrom(sensorUnit)
                .where(sensorUnit.chipId.eq(chipId))
                .fetchOne();

        if (unit == null) {
            throw new IllegalArgumentException("SensorUnit with chipId " + chipId + " not found");
        }

        // 최근 데이터 50개만 조회
        List<SensorData> recentData = queryFactory
                .selectFrom(sensorData)
                .join(sensorData.sensorUnit, sensorUnit).fetchJoin()
                .where(sensorUnit.chipId.eq(chipId))
                .orderBy(sensorData.sensedTime.desc())
                .limit(limit)
                .fetch();

        // DTO 변환
        List<SensorDataDTO> dataList = recentData.stream()
                .map(d -> new SensorDataDTO(d.getSensedData(), d.getSensedTime()))
                .collect(java.util.stream.Collectors.toList());

        return new SensorResponseDTO(
                unit.getChipId(),
                unit.getName(),
                unit.getLocation(),
                unit.getSensorType(),
                dataList
        );
    }

    /**
     * 사용자의 검색 옵션을 받아 쿼리를 실행하는 메서드
     * @param chipId 센서 칩셋 id. 해당 값을 기준으로 sensor_unit 테이블에서 알맞는 유닛 이름을 검색
     * @param type 연,월,일,시 에 해당하는 검색 조건
     * @param selectedDateStr 선택한 일자
     * @param direction before,after 가 존재하고, before 일 때는 선택 조건 이전 데이터를 가져오도록
     * @param count order by limit
     * @return
     */
    @Override
    public SensorResponseDTO findSensorDataByConditions(String chipId, String type, String selectedDateStr, String direction, Integer count) {
        QSensorUnit sensorUnit = QSensorUnit.sensorUnit;
        QSensorData sensorData = QSensorData.sensorData;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime selectedDate = LocalDateTime.parse(selectedDateStr, formatter);

        SensorUnit unit = queryFactory
                .selectFrom(sensorUnit)
                .where(sensorUnit.chipId.eq(chipId))
                .fetchOne();

        if (unit == null) {
            throw new IllegalArgumentException("chipId not found");
        }

        BooleanExpression baseCondition = sensorData.sensorUnit.eq(unit);
        BooleanExpression timeCondition;

        switch (type) {
            case "hour" -> {
                if (direction == null) {
                    throw new IllegalArgumentException("hour 타입에는 direction 값이 필요합니다.");
                }
                timeCondition = direction.equals("before")
                        ? sensorData.sensedTime.loe(selectedDate)
                        : sensorData.sensedTime.goe(selectedDate);
            }

            case "day" -> {
                timeCondition = sensorData.sensedTime.year().eq(selectedDate.getYear())
                        .and(sensorData.sensedTime.month().eq(selectedDate.getMonthValue()))
                        .and(sensorData.sensedTime.dayOfMonth().eq(selectedDate.getDayOfMonth()));
            }

            case "month" -> {
                timeCondition = sensorData.sensedTime.year().eq(selectedDate.getYear())
                        .and(sensorData.sensedTime.month().eq(selectedDate.getMonthValue()));
            }

            case "year" -> {
                timeCondition = sensorData.sensedTime.year().eq(selectedDate.getYear());
            }

            default -> throw new IllegalArgumentException("지원하지 않는 type: " + type);
        }

        // direction이 있는 경우 정렬 기준 분기
        boolean hasDirection = List.of("hour", "day", "month", "year").contains(type) && direction != null;
        var orderSpecifier = (hasDirection && direction.equals("after"))
                ? sensorData.sensedTime.asc()
                : sensorData.sensedTime.desc();

        List<SensorData> filtered = queryFactory
                .selectFrom(sensorData)
                .where(baseCondition.and(timeCondition))
                .orderBy(orderSpecifier)
                .limit(count != null ? count : 50)
                .fetch();

        List<SensorDataDTO> dataList = filtered.stream()
                .map(d -> new SensorDataDTO(d.getSensedData(), d.getSensedTime()))
                .toList();

        return new SensorResponseDTO(
                unit.getChipId(),
                unit.getName(),
                unit.getLocation(),
                unit.getSensorType(),
                dataList
        );
    }

    @Override
    public List<AggregatedDataDTO> getMonthlyAveragesByYear(String chipId, int year) {

        List<Tuple> result = queryFactory
                .select(
                        data.sensedTime.month().as("month"),
                        data.sensedData.avg(),
                        data.sensedData.count()
                )
                .from(data)
                .join(data.sensorUnit, unit)
                .where(
                        unit.chipId.eq(chipId),
                        data.sensedTime.year().eq(year)
                )
                .groupBy(data.sensedTime.month())
                .orderBy(data.sensedTime.month().asc())
                .fetch();

        return result.stream()
                .map(tuple -> new AggregatedDataDTO(
                        tuple.get(0, Integer.class) + "월",
                        tuple.get(1, Double.class),
                        tuple.get(2, Long.class)
                ))
                .toList();
    }

    @Override
    public List<AggregatedDataDTO> getWeeklyAvgByMonth(String chipId, int year, int month) {
        List<Tuple> result = queryFactory
                .select(
                        data.sensedTime.week(),
                        data.sensedData.avg(),
                        data.sensedData.count()
                )
                .from(data)
                .join(data.sensorUnit, unit)
                .where(
                        unit.chipId.eq(chipId),
                        data.sensedTime.year().eq(year),
                        data.sensedTime.month().eq(month)
                )
                .groupBy(data.sensedTime.week())
                .orderBy(data.sensedTime.week().asc())
                .fetch();

        AtomicInteger weekIndex = new AtomicInteger(1); // 상대 주차 인덱스: 1주차부터

        return result.stream()
                .map(tuple -> {
                    Double avg = tuple.get(data.sensedData.avg());
                    Long count = tuple.get(data.sensedData.count());

                    String label = String.format("%d월 %d주차", month, weekIndex.getAndIncrement());
                    return new AggregatedDataDTO(label, avg, count);
                })
                .toList();
    }

    @Override
    public List<AggregatedDataDTO> getDailyAvgByWeek(String chipId, int year, int month, int week) {
        QSensorUnit unit = QSensorUnit.sensorUnit;
        QSensorData data = QSensorData.sensorData;

        // 주차 계산: 월 첫째 주 기준 해당 주차의 날짜 범위 계산
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate weekStart = firstDay.plusWeeks(week - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        BooleanExpression condition = unit.chipId.eq(chipId)
                .and(data.sensedTime.between(weekStart.atStartOfDay(), weekEnd.atTime(23, 59, 59)));

        return queryFactory
                .select(
                        data.sensedTime.dayOfMonth(),
                        data.sensedData.avg(),
                        data.sensedData.count()
                )
                .from(data)
                .join(data.sensorUnit, unit)
                .where(condition)
                .groupBy(data.sensedTime.dayOfMonth())
                .orderBy(data.sensedTime.dayOfMonth().asc())
                .fetch()
                .stream()
                .map(tuple -> new AggregatedDataDTO(
                        tuple.get(data.sensedTime.dayOfMonth()) + "일",
                        tuple.get(data.sensedData.avg()),
                        tuple.get(data.sensedData.count())
                ))
                .toList();
    }

    @Override
    public List<AggregatedDataDTO> getHourlyAvgByDay(String chipId, int year, int month, int day) {
        List<Tuple> result = queryFactory
                .select(
                        data.sensedTime.hour(),
                        data.sensedData.avg(),
                        data.sensedData.count()
                )
                .from(data)
                .join(data.sensorUnit, unit)
                .where(
                        unit.chipId.eq(chipId),
                        data.sensedTime.year().eq(year),
                        data.sensedTime.month().eq(month),
                        data.sensedTime.dayOfMonth().eq(day)
                )
                .groupBy(data.sensedTime.hour())
                .orderBy(data.sensedTime.hour().asc())
                .fetch();

        return result.stream()
                .map(tuple -> new AggregatedDataDTO(
                        tuple.get(0, Integer.class) + "시",
                        tuple.get(1, Double.class),
                        tuple.get(2, Long.class)
                ))
                .toList();
    }

    @Override
    public List<AggregatedDataDTO> getRawDataByHour(String chipId, LocalDateTime dateTime) {
        List<SensorData> result = queryFactory
                .selectFrom(data)
                .join(data.sensorUnit, unit).fetchJoin()
                .where(
                        unit.chipId.eq(chipId),
                        data.sensedTime.year().eq(dateTime.getYear()),
                        data.sensedTime.month().eq(dateTime.getMonthValue()),
                        data.sensedTime.dayOfMonth().eq(dateTime.getDayOfMonth()),
                        data.sensedTime.hour().eq(dateTime.getHour())
                )
                .orderBy(data.sensedTime.asc())
                .fetch();

        return result.stream()
                .map(d -> new AggregatedDataDTO(
                        d.getSensedTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                        (double) d.getSensedData(),
                        1L
                ))
                .collect(Collectors.toList());
    }



}