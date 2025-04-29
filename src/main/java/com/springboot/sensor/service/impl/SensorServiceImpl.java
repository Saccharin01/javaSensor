package com.springboot.sensor.service.impl;

import com.springboot.sensor.data.entity.SensorData;
import com.springboot.sensor.data.entity.SensorUnit;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.springboot.sensor.service.SensorService;
import com.springboot.sensor.data.repository.SensorDataRepository;
import com.springboot.sensor.data.repository.SensorUnitRepository;
import com.springboot.sensor.data.dto.SensorRequestDTO;

import java.time.LocalDateTime;
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
        SensorUnit unit = sensorUnitRepository.findByChipId(data.getChipId())
                .orElseGet(() -> {
                    SensorUnit newUnit = new SensorUnit();
                    newUnit.setChipId(data.getChipId());
                    newUnit.setName(data.getName());
                    newUnit.setLocation(data.getLocation());
                    return sensorUnitRepository.save(newUnit);
                });

        // 2. 시간 검증 또는 서버 시간으로 대체
        LocalDateTime timestamp = data.getSensedTime();
        if (timestamp == null || timestamp.getYear() < 2001 || timestamp.getYear() > 2030) {
            timestamp = LocalDateTime.now();
        }

        // 3. 센서 데이터 저장부
        SensorData sensorData = new SensorData();
        sensorData.setSensorUnit(unit); // 연관관계
        sensorData.setSensedData(data.getSensedData());
        sensorData.setSensedTime(timestamp);

        sensorDataRepository.save(sensorData);

        // 4. 요청 완료 시 확인을 위해 데이터 반환과 data 객체에 서버에서 부여한 시간 추가
        data.setSensedTime(timestamp);
        return data;
    }
    public List<String> getSensorUnitIds() {
        return sensorUnitRepository.getSensorUnitIds();
    }




    @Override
    public List<SensorRequestDTO>getSenseorData(){
        return null;
    }

}
