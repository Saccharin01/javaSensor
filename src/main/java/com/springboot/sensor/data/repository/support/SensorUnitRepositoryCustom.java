package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.dto.AggregatedDataDTO;
import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.entity.SensorUnit;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorUnitRepositoryCustom {
    SensorUnit findOrCreateSensorUnit(String chipId, String name, String location);

    void saveSensorData(SensorUnit unit, int sensedData, LocalDateTime sensedTime);

    List<String> getSensorUnitIds();

    SensorResponseDTO findSensorUnitWithDataByChipId(String chipId);

    //유닛 선택 시 기본적으로 가장 최신 데이터에서 50개만 잘라서 넘겨주는 메서드
    SensorResponseDTO findSensorDataLimit(String chipId, int limit);

    //사용자의 요구에 따라 검색 기능을 수행하는 메서드
    SensorResponseDTO findSensorDataByConditions(String chipId, String type, String selectedDateStr, String direction, Integer count);

    //연도 검색 시 해당 년도의 각 월별 평균치를 가져옴
    List<AggregatedDataDTO> getMonthlyAveragesByYear(String chipId, int year);

    //월별 검색 시 각 해당 월차의 각 주차별 평균치를 가져옴
    List<AggregatedDataDTO> getWeeklyAvgByMonth(String chipId, int year, int month);

    //일차 별 검색 시 해당 일차의 시간 별 평균치를 가져옴
    List<AggregatedDataDTO> getHourlyAvgByDay(String chipId, int year, int month, int day);

    //주차 별 검색 시 해당 주차의 각 일별 평균치
    List<AggregatedDataDTO> getDailyAvgByWeek(String chipId, int year, int month, int week);

    //시간 검색에 해당하는 메서드
    List<AggregatedDataDTO> getRawDataByHour(String chipId, LocalDateTime dateTime);
}
