package com.springboot.sensor.data.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.sensor.data.dto.SensorDataDTO;
import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.entity.QSensorData;
import com.springboot.sensor.data.entity.QSensorUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SensorUnitRepositoryCustomImpl implements SensorUnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
                dataList
        );
    }
}