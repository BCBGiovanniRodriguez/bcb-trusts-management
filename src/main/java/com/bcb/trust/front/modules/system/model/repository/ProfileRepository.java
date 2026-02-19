package com.bcb.trust.front.modules.system.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.CatalogProfileEntity;

@Repository
public interface ProfileRepository extends JpaRepository<CatalogProfileEntity, Long> {

    Optional<CatalogProfileEntity> findOneByCode(String code);

}
