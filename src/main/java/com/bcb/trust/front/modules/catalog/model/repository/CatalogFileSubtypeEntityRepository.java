package com.bcb.trust.front.modules.catalog.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogFileSubtypeEntity;

@Repository
public interface CatalogFileSubtypeEntityRepository extends JpaRepository<CatalogFileSubtypeEntity, Long> {

}
