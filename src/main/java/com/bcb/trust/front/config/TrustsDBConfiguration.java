package com.bcb.trust.front.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "trustsEntityManagerFactory",
    transactionManagerRef = "trustsTransactionManager",
    basePackages = {
        "com.bcb.trust.front.model.trusts.*",
        "com.bcb.trust.front.modules.catalog.model.*",
        "com.bcb.trust.front.modules.request.model.*",
        "com.bcb.trust.front.modules.system.model.*",
        "com.bcb.trust.front.modules.trust.model.*",
        "com.bcb.trust.front.modules.configuration.model.*",
    }
)
public class TrustsDBConfiguration {

    @Primary
    @Bean(name = "trustsDatasource")
    @ConfigurationProperties("trusts.datasource")
    public DataSource trustDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "trustsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
        @Qualifier("trustsDatasource") DataSource trustDataSource) {
        //HashMap<String, Object> properties = new HashMap<>();
        //properties.put("hibernate.hbm2ddl.auto", "update");
        //properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        //properties.put("hibernate.connection.username", "root");
        //properties.put("hibernate.connection.password", "root");
        //properties.put("jakarta.persistence.jdbc.url", "jdbc:mysql://db:3306/trust");
        //properties.put("hibernate.connection.username", "gralrodriguez");
        //properties.put("hibernate.connection.password", "Hrodriguezr0800/+");
        //properties.put("jakarta.persistence.jdbc.url", "jdbc:mysql://localhost:3306/trusts");
        
        return builder.dataSource(trustDataSource)
                //.properties(properties)
                .packages(
                    "com.bcb.trust.front.model.trusts.entity", 
                    "com.bcb.trust.front.modules.catalog.model.entity",
                    "com.bcb.trust.front.modules.request.model.entity",
                    "com.bcb.trust.front.modules.system.model.entity",
                    "com.bcb.trust.front.modules.trust.model.entity",
                    "com.bcb.trust.front.modules.configuration.model.entity"
                ).persistenceUnit("trusts")
                .build();
    }

    @Primary
    @Bean(name = "trustsTransactionManager")
    public PlatformTransactionManager transactionManager(
        @Qualifier("trustsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(name = "trustNamedParameterJdbcTemplate")
    @DependsOn("trustsDatasource")
    public NamedParameterJdbcTemplate trustNamedParameterJdbcTemplate(@Qualifier("trustsDatasource") DataSource trustDataSource) {
        return new NamedParameterJdbcTemplate(trustDataSource);
    }

}
