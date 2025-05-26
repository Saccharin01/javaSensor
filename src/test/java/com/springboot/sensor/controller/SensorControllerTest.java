package com.springboot.sensor.controller;


import com.springboot.sensor.service.impl.SensorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorController.class)
public class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    SensorServiceImpl sensorService;

    @Test
    @DisplayName("Mock Mvc를 사용한 Sensor 데이터 테스트")
    void getSensorTest() throws Exception {
        given(sensorService.getSensorUnitIds()).willReturn(
                List.of(
                        "23ce79",
                        "24dm77",
                        "execTest0011",
                        "talend-test-01",
                        "talend-test-02"
                ));

        mockMvc.perform(
                get("/sensor/units"))
                .andExpect(
                        status().isOk())
                .andExpect(jsonPath(
                        "$.chipIds").exists()).andDo(print());

        verify(sensorService).getSensorUnitIds();

    }
}
