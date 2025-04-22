package com.springboot.sensor.service.impl;

import com.springboot.advanced_jpa.data.dao.ProductDAO;
import com.springboot.advanced_jpa.data.dto.ProductDTO;
import com.springboot.advanced_jpa.data.entity.Product;

import com.springboot.advanced_jpa.data.repository.QProductRepository;
import com.springboot.sensor.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.advanced_jpa.data.dto.ProductResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final QProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO, QProductRepository productRepository) {
        this.productDAO = productDAO;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDTO> getProductALL(){
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> new ProductResponseDTO(
                        product.getNumber(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock(),
                        product.getCreateAt(),
                        product.getUpdateAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDTO getProduct(Long number) {


        //todo Builder 패턴으로 변경도 고려 해봐야 함
        //todo ProductDAO의 selectProduct 인터페이스는 추후 예외 객체를 반환하도록 변경될 수 있음

        Product product = productDAO.selectProduct(number);

        ProductResponseDTO response = new ProductResponseDTO();

        response.setNumber(product.getNumber());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());

        return response;
    }

    @Override
    public ProductResponseDTO saveProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCreateAt(LocalDateTime.now());
        product.setUpdateAt(LocalDateTime.now());

        Product saveProduct = productDAO.insertProduct(product);
        return null;
    }

    @Override
    public ProductResponseDTO changeProductName(Long number, String name) throws Exception {

        Product changedItems = productDAO.updateProductName(number, name);


        ProductResponseDTO response = new ProductResponseDTO();
        response.setNumber(changedItems.getNumber());
        response.setName(changedItems.getName());
        response.setPrice(changedItems.getPrice());
        response.setStock(changedItems.getStock());

        return response;
    }

    @Override
    public void deleteProduct(Long number) throws Exception {
        productDAO.deleteProduct(number);

    }
}
