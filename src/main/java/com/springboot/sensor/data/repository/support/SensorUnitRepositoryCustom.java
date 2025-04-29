package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.entity.SensorUnit;

import java.util.List;
import java.util.Optional;

public interface SensorUnitRepositoryCustom {
//    Optional<SensorUnit> findUnitByChipIdWithQueryDsl(String chipId);

    List<String> getSensorUnitIds();

    SensorResponseDTO findSensorUnitWithDataByChipId(String chipId);
}
