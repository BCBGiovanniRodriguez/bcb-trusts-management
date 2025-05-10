package com.bcb.trust.front.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
/*
//@Configuration
//@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "bmtkfwebEntityManagerFactory",
    transactionManagerRef = "bmtkfwebTransactionManager",
    basePackages = {
        "com.bcb.trust.front.model.bmtkfweb.*"
    }
)*/
public class BmtkfwebDBConfiguration {

    @Bean
    @ConfigurationProperties("bmtkfweb.datasource")
    public DataSourceProperties bmtkfwebDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "bmtkfwebDatasource")
    @ConfigurationProperties("bmtkfweb.datasource")
    public DataSource bmtkfwebDataSource() {
        //return bmtkfwebDataSourceProperties().initializeDataSourceBuilder().build(); // Implement this
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "bmtkfwebEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
        @Qualifier("bmtkfwebDatasource") DataSource bmtkfwebDataSource) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.connection.username", "bursametrica");
        properties.put("hibernate.connection.password", "bursametrica");
        properties.put("jakarta.persistence.jdbc.url", "jdbc:oracle:thin:@10.20.50.200:1528:BMTKFWEB");
        properties.put("jakarta.persistence.jdbc.driver", "oracle.jdbc.OracleDriver");
        //properties.put("jakarta.persistence.jdbc.username", "bursametrica");
        //properties.put("jakarta.persistence.jdbc.password", "bursametrica");

        return builder.dataSource(bmtkfwebDataSource)
            .properties(properties)
            .packages("com.bcb.trust.front.model.bmtkfweb.entity")
            .persistenceUnit("bmtkfweb")
            .build();
    }

    @Bean(name = "bmtkfwebTransactionManager")
    public PlatformTransactionManager transactionManager(
        @Qualifier("bmtkfwebEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "bmtkfwebNamedParameterJdbcTemplate")
    @DependsOn("bmtkfwebDatasource")
    public NamedParameterJdbcTemplate bmtkfwebNamedParameterJdbcTemplate(@Qualifier("bmtkfwebDatasource") DataSource bmtkfwebDataSource) {
        return new NamedParameterJdbcTemplate(bmtkfwebDataSource);
    }
    
}
