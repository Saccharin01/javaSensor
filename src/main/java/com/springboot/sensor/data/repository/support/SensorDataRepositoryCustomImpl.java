package com.springboot.sensor.data.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.sensor.data.entity.SensorData;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.springboot.sensor.data.entity.QSensorData;
import com.springboot.sensor.data.entity.QSensorUnit;
@Repository
public class SensorDataRepositoryCustomImpl implements SensorDataRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SensorDataRepositoryCustomImpl(
            @Qualifier("sensorEntityManager") EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SensorData> findRecentDataByChipId(String chipId, int limit){
        QSensorData data = QSensorData.sensorData;
        QSensorUnit unit = QSensorUnit.sensorUnit;

        return queryFactory.selectFrom(data)
                .join(data.sensorUnit, unit).fetchJoin()
                .where(unit.chipId.eq(chipId))
                .orderBy(data.sensedTime.desc())
                .limit(limit)
                .fetch();
    }
}
