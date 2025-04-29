package com.springboot.sensor.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class SensorUnitIdDTO {
    private List<String> chipIds;
}