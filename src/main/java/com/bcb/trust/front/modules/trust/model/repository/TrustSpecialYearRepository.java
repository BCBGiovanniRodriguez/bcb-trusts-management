package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialYearEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;

@Repository
public interface TrustSpecialYearRepository extends JpaRepository<TrustSpecialYearEntity, Long> {

    List<TrustSpecialYearEntity> findAllByTrustEntity(TrustTrustEntity trustEntity);
}
