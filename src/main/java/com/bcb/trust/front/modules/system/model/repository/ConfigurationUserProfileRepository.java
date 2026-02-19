package com.bcb.trust.front.modules.system.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.ConfigurationUserProfileEntity;
import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;

@Repository
public interface ConfigurationUserProfileRepository extends JpaRepository<ConfigurationUserProfileEntity, Long> {

    List<ConfigurationUserProfileEntity> findByUserEntity(CatalogUserEntity systemUser);

    List<ConfigurationUserProfileEntity> findByProfileEntity(CatalogProfileEntity systemProfile);

}
