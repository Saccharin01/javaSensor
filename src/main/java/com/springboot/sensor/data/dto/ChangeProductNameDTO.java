package com.springboot.sensor.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProductNameDTO {
    private Long number;
    private String name;

    public ChangeProductNameDTO(Long number, String name) {
        this.number = number;
        this.name = name;
    }

    public ChangeProductNameDTO(){}

}
