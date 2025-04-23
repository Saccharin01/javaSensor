package com.springboot.sensor.controller;

import com.springboot.sensor.data.dto.SensorRequestDTO;
import com.springboot.sensor.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/sensor")
public class SensorController {
    private final SensorService sensorService;


    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }



    @Operation(
            summary = "test",
            description = "test"
    )
    @PostMapping
    public ResponseEntity<SensorRequestDTO> getData(@RequestBody SensorRequestDTO requestDTO) {
        SensorRequestDTO sensorData = sensorService.postSensorData(requestDTO); //

        return ResponseEntity.status(HttpStatus.OK).body(sensorData);

    }
}
