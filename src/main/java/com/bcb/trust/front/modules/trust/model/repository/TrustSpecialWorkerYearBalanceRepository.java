package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerYearBalanceEntity;

@Repository
public interface TrustSpecialWorkerYearBalanceRepository extends JpaRepository<TrustSpecialWorkerYearBalanceEntity, Long> {

    List<TrustSpecialWorkerYearBalanceEntity> findByWorkerEntity(TrustSpecialWorkerEntity workerEntity);

}
