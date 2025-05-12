package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.entity.SensorUnit;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorUnitRepositoryCustom {
    SensorUnit findOrCreateSensorUnit(String chipId, String name, String location);

    void saveSensorData(SensorUnit unit, int sensedData, LocalDateTime sensedTime);

    List<String> getSensorUnitIds();

    SensorResponseDTO findSensorUnitWithDataByChipId(String chipId);

    //유닛 선택 시 기본적으로 가장 최신 데이터에서 50개만 잘라서 넘겨주는 메서드
    SensorResponseDTO findSensorDataLimit(String chipId, int limit);

    //사용자의 요구에 따라 검색 기능을 수행하는 메서드
    SensorResponseDTO findSensorDataByConditions(String chipId, String type, String selectedDateStr, String direction, Integer count);
}
