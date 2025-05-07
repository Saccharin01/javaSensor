package com.springboot.sensor.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "sensor_unit")
public class SensorUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chipPk;

    @Column(nullable = false, unique = true)
    private String chipId;

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    @Column(name = "sensor_type")
    private String sensorType;

    @OneToMany(mappedBy = "sensorUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorData> dataList = new ArrayList<>();

}
