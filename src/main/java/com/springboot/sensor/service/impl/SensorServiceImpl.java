package com.springboot.sensor.service.impl;

import com.springboot.sensor.data.entity.SensorData;
import com.springboot.sensor.data.entity.SensorUnit;
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
    public SensorRequestDTO postSensorData(SensorRequestDTO data) {

        // 1. chip_id로 센서 유닛 조회
        SensorUnit unit = sensorUnitRepository.findByChipId(data.getChip_id())
                .orElseGet(() -> {
                    SensorUnit newUnit = new SensorUnit();
                    newUnit.setChipId(data.getChip_id());
                    newUnit.setName(data.getName());
                    newUnit.setLocation(data.getLocation());
                    return sensorUnitRepository.save(newUnit);
                });

        // 2. 센서 데이터 저장
        SensorData sensorData = new SensorData();
        sensorData.setSensorUnit(unit); // 연관관계
        sensorData.setSensedData(data.getData());
        sensorData.setSensedTime(data.getSensed_time());

        sensorDataRepository.save(sensorData);

        // 3. 테스트 목적: 입력 받은 DTO 그대로 반환
        return data;
    }

    @Override
    public List<SensorRequestDTO>getSenseorData(){
        return null;
    }

}
