package com.springboot.sensor.controller;

import com.springboot.sensor.data.entity.Product;
import com.springboot.sensor.data.repository.support.ProductRepositorySupport;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    private final ProductRepositorySupport productRepositoryCustom;

    @Autowired
    public TestController(ProductRepositorySupport productRepositoryCustom) {
        this.productRepositoryCustom = productRepositoryCustom;
    }

    @Operation(
            summary = "테스트용 엔트포인트 매핑",
            description = "url 파라미터 name 을 이용해서 데이터베이스에 해당 이름이 존재하는 요소를 가져오는 테스트"
    )
    @GetMapping
    public List<Product> test(String name) {
        return productRepositoryCustom.findByName(name);
    }

}
