package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialPeriodEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;

@Repository
public interface TrustSpecialPeriodRepository extends JpaRepository<TrustSpecialPeriodEntity, Long> {

    List<TrustSpecialPeriodEntity> findAllByTrustEntity(TrustTrustEntity trustEntity);

}
