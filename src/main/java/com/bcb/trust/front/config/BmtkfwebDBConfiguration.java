package com.bcb.trust.front.config;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "bmtkfwebEntityManagerFactory",
    transactionManagerRef = "bmtkfwebTransactionManager",
    basePackages = {
        "com.bcb.trust.front.model.bmtkfweb.*"
    }
)
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
    public LocalContainerEntityManagerFactoryBean bmtkfwebEntityManagerFactory(EntityManagerFactoryBuilder builder,
        @Qualifier("bmtkfwebDatasource") DataSource bmtkfwebDataSource) {

        return builder.dataSource(bmtkfwebDataSource)
            .packages("com.bcb.trust.front.model.bmtkfweb.*", "com.bcb.trust.front.modules.legacy.*")
            .persistenceUnit("bmtkfweb")
            .build();
    }

    @Bean(name = "bmtkfwebTransactionManager")
    public PlatformTransactionManager transactionManager(
        @Qualifier("bmtkfwebEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
            return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "bmtkfwebJdbcTemplate")
    @DependsOn("bmtkfwebDatasource")
    public JdbcTemplate bmtkfwebJdbcTemplate(@Qualifier("bmtkfwebDatasource") DataSource bmtkfwebDataSource) {
        return new JdbcTemplate(bmtkfwebDataSource);
    }

    @Bean(name = "bmtkfwebNamedParameterJdbcTemplate")
    @DependsOn("bmtkfwebDatasource")
    public NamedParameterJdbcTemplate bmtkfwebNamedParameterJdbcTemplate(@Qualifier("bmtkfwebDatasource") DataSource bmtkfwebDataSource) {
        return new NamedParameterJdbcTemplate(bmtkfwebDataSource);
    }
    
}
