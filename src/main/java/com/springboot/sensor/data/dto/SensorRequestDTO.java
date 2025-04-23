package com.springboot.sensor.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SensorRequestDTO {

    private String chip_id;
    private int data;
    private String name;
    private String location;
    private LocalDateTime sensed_time;


    public SensorRequestDTO(
            String chip_id,
            int data,String name,
            String location,
            LocalDateTime sensed_time)
    {
        this.chip_id = chip_id;
        this.data = data;
        this.name = name;
        this.location = location;
        this.sensed_time = sensed_time;

    }
}