package com.springboot.sensor.controller;

import com.springboot.sensor.data.dto.ChangeProductNameDTO;
import com.springboot.sensor.data.dto.ProductDTO;
import com.springboot.sensor.data.dto.ProductResponseDTO;
import com.springboot.sensor.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "상품 조회",
            description = "상품의 id 값을 이용해 해당 상품의 정보를 가져옵니다."
    )
    @GetMapping
    public ResponseEntity<ProductResponseDTO> getProduct(Long number) {

        ProductResponseDTO searchedItem = productService.getProduct(number);

        return ResponseEntity.status(HttpStatus.OK).body(searchedItem);

    }

    @Operation(
            summary = "상품 등록",
            description = "새로운 상품 정보를 입력받아 DB에 저장합니다."
    )
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductDTO product) {
        ProductResponseDTO response = productService.saveProduct(product);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "상품 이름 변경",
            description = "상품의 id 값을 이용해 해당하는 상품의 이름을 변경합니다. </br>" +
                    "변경 대상이 없을 경우 오류가 발생합니다."
    )
    @PutMapping
    public ResponseEntity<ProductResponseDTO> changeProductName(
            @RequestBody ChangeProductNameDTO changeProduct) throws Exception {

        ProductResponseDTO response = productService.changeProductName(

                changeProduct.getNumber(),
                changeProduct.getName()
        );

                return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "상품 삭제",
            description = "조회된 상품을 삭제합니다."
    )
    @DeleteMapping
    public ResponseEntity<String> deleteProduct(Long number) throws Exception {
        productService.deleteProduct(number);

        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }

    @Operation(
            summary = "상품 전체 조회",
            description = "데이터베이스에 존재하는 모든 데이터를 가져옵니다."
    )
    @GetMapping(value = "/all")
    public List<ProductResponseDTO> getAllProduct() {
        return productService.getProductALL();
    }
}
