package com.bcb.trust.front.modules.system.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.CatalogResourceEntity;

@Repository
public interface ResourceRepository extends JpaRepository<CatalogResourceEntity, Long> {

    CatalogResourceEntity findOneByCode(String code);

    List<CatalogResourceEntity> findByModule(Integer module);

    List<CatalogResourceEntity> findByPathAndMethod(String path, String method);
}
