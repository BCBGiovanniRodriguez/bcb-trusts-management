package com.bcb.trust.front.modules.admin.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.admin.model.entity.CatalogDocumentTypeEntity;

@Repository
public interface CatalogDocumentTypeEntityRepository extends JpaRepository<CatalogDocumentTypeEntity, Long> {

}
