package com.springboot.sensor.data.dto;

import lombok.Getter;

@Getter
public class ProductDTO {
    private final String name;
    private final int price;
    private final int stock;

    public ProductDTO(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

}
