package com.bcb.trust.front.modules.configuration.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.configuration.model.entity.ConfigurationEndpointEntity;

@Repository
public interface ConfigurationEndpointRepository extends JpaRepository<ConfigurationEndpointEntity, Long> {

}
