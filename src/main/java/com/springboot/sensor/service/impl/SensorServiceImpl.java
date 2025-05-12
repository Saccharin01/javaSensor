package com.springboot.sensor.service.impl;

import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.entity.SensorUnit;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.springboot.sensor.service.SensorService;
import com.springboot.sensor.data.repository.SensorUnitRepository;
import com.springboot.sensor.data.dto.SensorRequestDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class SensorServiceImpl implements SensorService {

    private final SensorUnitRepository sensorUnitRepository;

    @Autowired
    public SensorServiceImpl(
            SensorUnitRepository sensorUnitRepository)
    {
        this.sensorUnitRepository = sensorUnitRepository;
    }

    @Override
    @Transactional
    public SensorRequestDTO postSensorData(SensorRequestDTO data) {
        // 1. 시간 검증
        LocalDateTime timestamp = data.getSensedTime();
        if (timestamp == null || timestamp.getYear() < 2001 || timestamp.getYear() > 2030) {
            timestamp = LocalDateTime.now();
        }

        // 2. 유닛 조회 및 없으면 생성
        SensorUnit unit = sensorUnitRepository.findOrCreateSensorUnit(
                data.getChipId(), data.getName(), data.getLocation());

        // 3. 데이터 저장
        sensorUnitRepository.saveSensorData(unit, data.getSensedData(), timestamp);

        // 4. 반환 데이터 세팅
        data.setSensedTime(timestamp);
        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public SensorResponseDTO getSensorDataLimit(String chipId) {
        return sensorUnitRepository.findSensorDataLimit(chipId, 50);
    }

    @Override
    public SensorResponseDTO getSensorDataWithQuery(String chipId, String type, String selectedDate, String direction, Integer count) {
        return sensorUnitRepository.findSensorDataByConditions(chipId, type, selectedDate, direction, count);
    }

    //센서 id 에 해당하는 모든 데이터를 반환하도록 되어있는데 추후 사용 가능성 있음
    public SensorResponseDTO getSensorDataByChipId(String chipId) {
        return sensorUnitRepository.findSensorUnitWithDataByChipId(chipId);
    }


    public List<String> getSensorUnitIds() {
        return sensorUnitRepository.getSensorUnitIds();
    }


}
