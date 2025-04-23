package com.springboot.sensor.data.repository;

import com.springboot.sensor.data.entity.SensorData;
import com.springboot.sensor.data.repository.support.SensorUnitRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SensorDataRepository extends JpaRepository<SensorData, Long>,
        QuerydslPredicateExecutor<SensorData>,
        SensorUnitRepositoryCustom {}