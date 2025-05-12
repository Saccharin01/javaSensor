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
            summary = "특정 센서 유닛의 데이터를 가져오는 엔드포인트",
            description = "쿼리스트링 chipId를 기반으로 센서 데이터를 조회합니다."
    )
    @GetMapping("/units/basic")
    public ResponseEntity<SensorResponseDTO> getSensorDataBasic(@RequestParam String chipId) {
        SensorResponseDTO response = sensorService.getSensorDataLimit(chipId);
        return ResponseEntity.ok(response);
    }


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
