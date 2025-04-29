package com.springboot.sensor.service;

import com.springboot.sensor.data.dto.SensorRequestDTO;
import com.springboot.sensor.data.dto.SensorUnitIdDTO;

import java.util.List;


public interface SensorService {

    SensorRequestDTO postSensorData(SensorRequestDTO data); // 아두이노 보드에서 백엔드로 데이터를 전송할 때

    List<SensorRequestDTO> getSenseorData(); // 해당 결과값을 바탕으로 ApexChart 에서 차트를 그려야 함

    List<SensorUnitIdDTO> getSensorUnitIds();
}
