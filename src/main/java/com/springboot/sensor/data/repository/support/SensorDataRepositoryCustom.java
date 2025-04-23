package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.entity.SensorData;
import java.util.List;

public interface SensorDataRepositoryCustom {
    List<SensorData> findRecentDataByChipId(String chipId, int limit);
}