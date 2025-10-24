package com.bcb.trust.front.modules.trust.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustTrustWorkerEntity;

@Repository
public interface TrustTrustWorkerRepository extends JpaRepository<TrustTrustWorkerEntity, Long> {

    @Query("SELECT e FROM TrustTrustWorkerEntity e WHERE e.number = (SELECT MAX(e2.number) FROM TrustTrustWorkerEntity e2)")
    TrustTrustWorkerEntity findEntityWithMaxNumber();
}
