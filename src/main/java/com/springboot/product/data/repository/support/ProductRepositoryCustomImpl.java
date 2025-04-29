package com.springboot.product.data.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.product.data.entity.Product;
import com.springboot.product.data.entity.QProduct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import jakarta.persistence.EntityManager;
import jakarta.annotation.PostConstruct;

import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryCustomImpl(@Qualifier("productEntityManager") EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Product> findByName(String name) {
        QProduct product = QProduct.product;

        return queryFactory
                .selectFrom(product)
                .where(product.name.contains(name))
                .fetch();
    }
}