package com.springboot.product.data.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponseDTO {
    private Long number;
    private String name;
    private int price;
    private int stock;
    private LocalDateTime create_at;
    private LocalDateTime update_at;

    public ProductResponseDTO() {}

    public ProductResponseDTO(Long number, String name, int price, int stock, LocalDateTime create_at, LocalDateTime update_at) {
        this.number = number;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.create_at = create_at;
        this.update_at = update_at;
    }
}
