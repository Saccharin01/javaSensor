package com.springboot.product.data.dao.impl;

import com.springboot.product.data.dao.ProductDAO;
import com.springboot.product.data.entity.Product;
import com.springboot.product.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProductDAOImpl implements ProductDAO {

    private final ProductRepository productRepository;

    @Autowired
    public ProductDAOImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product insertProduct(Product product) {
        Product insertedProduct = productRepository.save(product);
        return insertedProduct;
    }

    @Override
    public Product selectProduct(Long number) {

        //! Deprecated Method
        //todo findById Method 로 변경해야 하는데, findById 사용 시 예외 처리를 수행해야 함
        Product selected = productRepository.getById(number);
        return selected;
    }

    @Override
    public Product updateProductName(Long number, String name) throws Exception {
        Optional<Product> selected = productRepository.findById(number);

        Product updatedProduct;
        if (selected.isPresent()) {

            Product updateItem = selected.get();

            updateItem.setName(name);
            updateItem.setUpdateAt(LocalDateTime.now());

            updatedProduct = productRepository.save(updateItem);
        } else {
            throw new Exception("선택된 항목을 변경할 수 없습니다.");
        }

        return updatedProduct;
    }

    @Override
    public void deleteProduct(Long number) throws Exception {
        Optional<Product> selected = productRepository.findById(number);
        if (selected.isPresent()) {
            Product deleteItem = selected.get();

            productRepository.delete(deleteItem);
        } else {
            throw new Exception("선택된 항목을 삭제할 수 없습니다.");
        }

    }
}
