package com.springboot.sensor.data.repository;

import com.springboot.sensor.data.entity.SensorUnit;
import com.springboot.sensor.data.repository.support.SensorUnitCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SensorUnitRepository extends JpaRepository<SensorUnit, Long>,
        QuerydslPredicateExecutor<SensorUnit>,
        SensorUnitCustomRepository {}