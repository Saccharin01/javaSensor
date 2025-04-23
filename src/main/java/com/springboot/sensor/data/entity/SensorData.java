package com.springboot.sensor.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataId;

    @Column(name = "sensed_data", nullable = false)
    private int sensedData;

    @Column(name = "sensed_time", nullable = false)
    private LocalDateTime sensedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private SensorUnit sensorUnit;
}