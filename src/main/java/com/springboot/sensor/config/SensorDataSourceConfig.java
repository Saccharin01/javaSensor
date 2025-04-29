package com.springboot.sensor.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.springboot.sensor.data.repository",   // ✅ sensor 전용 repository 경로
        entityManagerFactoryRef = "sensorEntityManagerFactory",
        transactionManagerRef = "sensorTransactionManager"
)
@EntityScan(basePackages = "com.springboot.sensor.data.entity")
public class SensorDataSourceConfig {

    @Bean(name = "sensorDataSourceProperties")
    @ConfigurationProperties("spring.datasource.sensor")
    public DataSourceProperties sensorDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "sensorDataSource")
    public DataSource sensorDataSource(
            @Qualifier("sensorDataSourceProperties") DataSourceProperties properties
    ) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "sensorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sensorEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("sensorDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.springboot.sensor.data.entity")   // ✅ sensor 전용 entity 경로
                .persistenceUnit("sensor")                  // ✅ 다른 persistence unit 이름
                .build();
    }

    @Bean(name = "sensorTransactionManager")
    public PlatformTransactionManager sensorTransactionManager(
            @Qualifier("sensorEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    @Bean(name = "sensorEntityManager")
    public EntityManager sensorEntityManager(
            @Qualifier("sensorEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return entityManagerFactory.createEntityManager();
    }
}