package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.entity.SensorUnit;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorUnitRepositoryCustom {
//    Optional<SensorUnit> findUnitByChipIdWithQueryDsl(String chipId);

    SensorUnit findOrCreateSensorUnit(String chipId, String name, String location);

    void saveSensorData(SensorUnit unit, int sensedData, LocalDateTime sensedTime);

    List<String> getSensorUnitIds();

    SensorResponseDTO findSensorUnitWithDataByChipId(String chipId);

    SensorResponseDTO findSensorDataLimit(String chipId, int limit);
}
