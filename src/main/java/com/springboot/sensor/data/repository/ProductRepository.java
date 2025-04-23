package com.springboot.sensor.data.repository;

import com.springboot.sensor.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository  extends JpaRepository<Product, Long> {
    Optional<Product> findByNumber(Long number);
    List<Product> findAllByName(String name);
    Product queryByNumber(Long number);

    boolean existsByName(String name);
    boolean existsByNumber(Long number);

    long countByName(String name);

    void deleteByNumber(Long number);
    void deleteByName(String name);

    long removeByNumber(Long number);

    List<Product> findFirst5ByName(String name);
    List<Product> findTop10ByName(String name);

    Product findByNumberIs(Long number);
    Product findByNameEquals(String name);

    List<Product> findByUpdateAtIsNull();
}
