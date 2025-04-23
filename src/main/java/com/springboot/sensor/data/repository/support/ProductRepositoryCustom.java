package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findByName(String name);
}