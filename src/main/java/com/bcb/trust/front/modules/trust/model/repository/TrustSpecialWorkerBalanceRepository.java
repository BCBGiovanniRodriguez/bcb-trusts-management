package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialPeriodEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerBalanceEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerEntity;

@Repository
public interface TrustSpecialWorkerBalanceRepository extends JpaRepository<TrustSpecialWorkerBalanceEntity, Long> {

    List<TrustSpecialWorkerBalanceEntity> findAllByWorkerEntityAndPeriodEntity(TrustSpecialWorkerEntity workerEntity, TrustSpecialPeriodEntity periodEntity);

    List<TrustSpecialWorkerBalanceEntity> findByWorkerEntity(TrustSpecialWorkerEntity workerEntity);

}
