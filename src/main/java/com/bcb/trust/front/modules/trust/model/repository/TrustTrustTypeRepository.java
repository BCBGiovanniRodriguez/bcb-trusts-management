package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustCatalogTrustTypeEntity;

@Repository
public interface TrustTrustTypeRepository extends JpaRepository<TrustCatalogTrustTypeEntity, Long> {

    public List<TrustCatalogTrustTypeEntity> findByStatus(Integer status);
}
