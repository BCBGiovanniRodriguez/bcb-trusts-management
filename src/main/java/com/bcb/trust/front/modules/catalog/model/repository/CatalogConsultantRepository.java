package com.bcb.trust.front.modules.catalog.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogConsultant;

@Repository
public interface CatalogConsultantRepository extends JpaRepository<CatalogConsultant, Long> {

}
