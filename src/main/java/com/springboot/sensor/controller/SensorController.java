package com.springboot.sensor.controller;

import com.springboot.sensor.data.dto.SensorRequestDTO;
import com.springboot.sensor.data.dto.SensorResponseDTO;
import com.springboot.sensor.data.dto.SensorUnitIdDTO;
import com.springboot.sensor.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/sensor")
public class SensorController {
    private final SensorService sensorService;


    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }



    @Operation(
            summary = "센서 데이터를 전송하는 엔드포인트",
            description = "센서에서 측정된 데이터를 데이터베이스에 전송하는 메서드입니다"
    )
    @PostMapping(value = "/data")
    public ResponseEntity<SensorRequestDTO> getData(@RequestBody SensorRequestDTO requestDTO) {
        SensorRequestDTO sensorData = sensorService.postSensorData(requestDTO); //

        return ResponseEntity.status(HttpStatus.OK).body(sensorData);

    }

    @Operation(
            summary = "센서 유닛의 id 값을 가져오는 메서드입니다.",
            description = "테이블에 저장되어있는 센서의 id를 chipIds 키에 매핑하여 반환합니다."
    )
    @GetMapping( "/units")
    public ResponseEntity<SensorUnitIdDTO> getSensorUnitId() {
        List<String> chipIds = sensorService.getSensorUnitIds();

        return ResponseEntity.ok(new SensorUnitIdDTO(chipIds));
    }

    @Operation(
            summary = "최초 유닛 데이터를 제공하는 엔드포인트.",
            description = "최초 사용자가 페이지와 인터렉션 시 가장 최근의 데이터 50개를 가져올 수 있도록 합니다."
    )
    @GetMapping("/units/basic")
    public ResponseEntity<SensorResponseDTO> getSensorDataBasic(@RequestParam String chipId) {
        SensorResponseDTO response = sensorService.getSensorDataLimit(chipId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "검색 조건을 수행하는 엔드포인트.",
            description = "사용자의 검색 요구 조건에 따라 검색 조건을 수행하여 응답하는 엔드포인트입니다."
    )

    @GetMapping("/units/search")
    public ResponseEntity<SensorResponseDTO> getSensorDataSearched (
            @RequestParam String chipId,
            @RequestParam String type,
            @RequestParam String selectedDate,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) Integer count
    ){
        SensorResponseDTO response = sensorService.getSensorDataWithQuery(chipId, type, selectedDate, direction, count);
        return ResponseEntity.ok(response);
    }
}
