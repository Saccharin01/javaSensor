package com.springboot.sensor.data.repository.support;

import com.springboot.sensor.data.entity.Product;
import com.springboot.sensor.data.entity.QProduct;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ProductRepositoryCustomImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryCustomImpl() {
        super(Product.class);
    }

    @Override
    public List<Product> findByName(String name){
        QProduct product = QProduct.product;

        List<Product> result = from(product)
                .where(product.name.contains(name))
                .select(product)
                .fetch();


        return result;
    }
}
