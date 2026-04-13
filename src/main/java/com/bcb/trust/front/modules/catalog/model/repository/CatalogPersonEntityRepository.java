package com.bcb.trust.front.modules.catalog.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;


@Repository
public interface CatalogPersonEntityRepository extends JpaRepository<CatalogPersonEntity, Long> {
    
    Optional<CatalogPersonEntity> findOneByRfc(String rfc);

    List<CatalogPersonEntity> findByFirstNameStartsWith(String firstName);

    List<CatalogPersonEntity> findBySecondNameStartsWith(String secondName);

    List<CatalogPersonEntity> findByLastNameStartsWith(String lastName);

    List<CatalogPersonEntity> findBySecondLastNameStartsWith(String secondLastName);

    List<CatalogPersonEntity> findByRfcStartsWith(String rfc);

}
