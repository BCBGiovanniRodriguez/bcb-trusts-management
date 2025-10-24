package com.bcb.trust.front.modules.catalog.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;


@Repository
public interface CatalogPersonEntityRepository extends JpaRepository<CatalogPersonEntity, Long> {
    
    Optional<CatalogPersonEntity> findOneByRfc(String rfc);

}
