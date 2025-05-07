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
import java.util.List;

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
}