package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("productRepositorySupport")
public interface ProductRepositorySupport extends JpaRepository<Product, Long>, ProductRepositoryCustom{
}
