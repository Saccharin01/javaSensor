package com.springboot.sensor.service;

import com.springboot.advanced_jpa.data.dto.ProductDTO;
import com.springboot.advanced_jpa.data.dto.ProductResponseDTO;
import java.util.List;

public interface ProductService {

    List<ProductResponseDTO> getProductALL();

    ProductResponseDTO getProduct(Long number);

    ProductResponseDTO saveProduct(ProductDTO product);

    ProductResponseDTO changeProductName(Long number, String name) throws Exception;

    void deleteProduct(Long number) throws Exception;
}
