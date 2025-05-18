package com.springboot.sensor.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AggregatedDataDTO {
    private String label;        // "1월", "2월
    private Double averageValue; // 월별 평균값
    private Long sampleCount;    // 샘플 수
}