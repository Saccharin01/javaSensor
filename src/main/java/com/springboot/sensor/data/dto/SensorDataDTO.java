package com.springboot.sensor.data.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SensorDataDTO {
    private int sensedData;
    private LocalDateTime sensedTime;

    public SensorDataDTO(int sensedData, LocalDateTime sensedTime) {
        this.sensedData = sensedData;
        this.sensedTime = sensedTime;
    }
}
