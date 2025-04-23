package com.springboot.sensor.data.repository;

import com.springboot.sensor.data.entity.SensorUnit;
import com.springboot.sensor.data.repository.support.SensorUnitRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface SensorUnitRepository extends JpaRepository<SensorUnit, Long>, QuerydslPredicateExecutor<SensorUnit>,
        SensorUnitRepositoryCustom {
            Optional<SensorUnit> findByChipId(String chipId);
}