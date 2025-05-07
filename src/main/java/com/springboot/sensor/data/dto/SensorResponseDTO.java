package com.springboot.sensor.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SensorResponseDTO {

    private String chipId;               // 센서 식별자
    private String name;                 // 센서 이름
    private String location;             // 설치 장소
    private String sensorType;
    private List<SensorDataDTO> data;     // 측정 데이터 목록

    public SensorResponseDTO(String chipId, String name, String location, String sensorType,List<SensorDataDTO> data) {
        this.chipId = chipId;
        this.name = name;
        this.location = location;
        this.sensorType = sensorType;
        this.data = data;
    }
}
