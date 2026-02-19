package com.bcb.trust.front.modules.system.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationProfileResourceEntity;

import java.util.List;

@Repository
public interface ProfileResourceRepository extends JpaRepository<ConfigurationProfileResourceEntity, Long> {

    List<ConfigurationProfileResourceEntity> findByProfileEntity(CatalogProfileEntity profileEntity);

}
