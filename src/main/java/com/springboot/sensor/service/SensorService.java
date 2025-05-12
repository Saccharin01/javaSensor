package com.springboot.sensor.service;

import com.springboot.sensor.data.dto.SensorRequestDTO;
import com.springboot.sensor.data.dto.SensorResponseDTO;

import java.util.List;


public interface SensorService {

    SensorRequestDTO postSensorData(SensorRequestDTO data); // 아두이노 보드에서 백엔드로 데이터를 전송할 때

    List<String>getSensorUnitIds();

    SensorResponseDTO getSensorDataLimit(String chipId);

    SensorResponseDTO getSensorDataByChipId(String chipId); // 해당 결과값을 바탕으로 ApexChart 에서 차트를 그려야 함

    SensorResponseDTO getSensorDataWithQuery(String chipId, String type, String selectedDate, String direction, Integer count);
}
