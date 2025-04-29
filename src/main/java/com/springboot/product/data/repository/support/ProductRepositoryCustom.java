package com.springboot.product.data.repository.support;

import com.springboot.product.data.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findByName(String name);
}