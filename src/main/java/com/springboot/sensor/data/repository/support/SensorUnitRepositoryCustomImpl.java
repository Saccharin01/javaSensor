package com.springboot.sensor.data.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.querydsl.core.types.dsl.BooleanExpression;

@Repository
@RequiredArgsConstructor
public class SensorUnitRepositoryCustomImpl implements SensorUnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager entityManager;

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

        // 기준 시간 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime selectedDate = LocalDateTime.parse(selectedDateStr, formatter);

        // 유닛 조회
        SensorUnit unit = queryFactory
                .selectFrom(sensorUnit)
                .where(sensorUnit.chipId.eq(chipId))
                .fetchOne();

        if (unit == null) throw new IllegalArgumentException("chipId not found");

        // ❸ 조건 조립
        BooleanExpression baseCondition = sensorData.sensorUnit.eq(unit);

        BooleanExpression timeCondition = null;
        switch (type) {
            case "hour" -> timeCondition = direction.equals("before")
                    ? sensorData.sensedTime.loe(selectedDate)
                    : sensorData.sensedTime.goe(selectedDate);
            case "day" -> timeCondition = sensorData.sensedTime.year().eq(selectedDate.getYear())
                    .and(sensorData.sensedTime.month().eq(selectedDate.getMonthValue()))
                    .and(sensorData.sensedTime.dayOfMonth().eq(selectedDate.getDayOfMonth()));
            case "month" -> timeCondition = sensorData.sensedTime.year().eq(selectedDate.getYear())
                    .and(sensorData.sensedTime.month().eq(selectedDate.getMonthValue()));
            case "year" -> timeCondition = sensorData.sensedTime.year().eq(selectedDate.getYear());
        }

        // 최종 쿼리 실행
        List<SensorData> filtered = queryFactory
                .selectFrom(sensorData)
                .where(baseCondition.and(timeCondition))
                .orderBy(sensorData.sensedTime.desc())
                .limit(count != null ? count : 50)
                .fetch();

        // DTO 반환
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
}