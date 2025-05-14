package com.springboot.sensor.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SensorRequestDTO {

    private String chipId;            // 센서 칩 ID
    private int sensedData;           // 측정된 수분 값
    private String name;              // 센서 이름
    private String location;          // 설치 장소
    private LocalDateTime sensedTime; // 측정 시간

    public SensorRequestDTO(
            String chipId,
            int sensedData,
            String name,
            String location,
            LocalDateTime sensedTime) {
        this.chipId = chipId;
        this.sensedData = sensedData;
        this.name = name;
        this.location = location;
        this.sensedTime = sensedTime;
    }
}