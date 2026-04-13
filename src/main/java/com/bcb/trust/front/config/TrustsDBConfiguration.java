package com.bcb.trust.front.config;

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
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "trustsEntityManagerFactory", transactionManagerRef = "trustsTransactionManager", basePackages = {
        "com.bcb.trust.front.model.trusts.*",
        "com.bcb.trust.front.modules.catalog.model.*",
        "com.bcb.trust.front.modules.admin.model.*",
        "com.bcb.trust.front.modules.request.model.*",
        "com.bcb.trust.front.modules.system.model.*",
        "com.bcb.trust.front.modules.trust.model.*",
        "com.bcb.trust.front.modules.sisbur.model.*",
        "com.bcb.trust.front.modules.configuration.model.*",
})
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

        return builder.dataSource(trustDataSource)
                .packages(
                        "com.bcb.trust.front.model.trusts.entity",
                        "com.bcb.trust.front.modules.catalog.model.entity",
                        "com.bcb.trust.front.modules.admin.model.entity",
                        "com.bcb.trust.front.modules.request.model.entity",
                        "com.bcb.trust.front.modules.system.model.entity",
                        "com.bcb.trust.front.modules.trust.model.entity",
                        "com.bcb.trust.front.modules.sisbur.model.entity",
                        "com.bcb.trust.front.modules.configuration.model.entity")
                .persistenceUnit("trusts")
                .build();
    }

    @Primary
    @Bean(name = "trustsTransactionManager")
    public PlatformTransactionManager transactionManager(@NonNull
            @Qualifier("trustsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Primary
    @Bean(name = "trustNamedParameterJdbcTemplate")
    @DependsOn("trustsDatasource")
    public NamedParameterJdbcTemplate trustNamedParameterJdbcTemplate( @NonNull
            @Qualifier("trustsDatasource") DataSource trustDataSource) {
        return new NamedParameterJdbcTemplate(trustDataSource);
    }

}
