package com.springboot.sensor.data.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
}