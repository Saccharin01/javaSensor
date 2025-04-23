package com.springboot.sensor.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.springboot.sensor.service.SensorService;
import com.springboot.sensor.data.repository.SensorDataRepository;
import com.springboot.sensor.data.repository.SensorUnitRepository;
import com.springboot.sensor.data.dto.SensorRequestDTO;

import java.util.List;


@Service
public class SensorServiceImpl implements SensorService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorUnitRepository sensorUnitRepository;

    @Autowired
    public SensorServiceImpl(
            SensorDataRepository sensorDataRepository,
            SensorUnitRepository sensorUnitRepository)
    {
        this.sensorDataRepository = sensorDataRepository;
        this.sensorUnitRepository = sensorUnitRepository;
    }

    @Override
    public SensorRequestDTO postSensorData(){
        return null;
    }

    @Override
    public List<SensorRequestDTO>getSenseorData(){
        return null;
    }

}
