package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.entity.SensorUnit;

import java.util.Optional;

public interface SensorUnitRepositoryCustom {
    Optional<SensorUnit> findUnitByChipIdWithQueryDsl(String chipId);
}
