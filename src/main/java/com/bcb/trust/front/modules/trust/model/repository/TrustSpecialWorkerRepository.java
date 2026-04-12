package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialWorkerEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;

@Repository
public interface TrustSpecialWorkerRepository extends JpaRepository<TrustSpecialWorkerEntity, Long> {

    Long countByTrustEntity(TrustTrustEntity trustEntity);

    TrustSpecialWorkerEntity findTopByTrustEntityOrderByCreatedAtDesc(TrustTrustEntity trustEntity);

    Optional<TrustSpecialWorkerEntity> findOneByAccount(String account);

    Optional<TrustSpecialWorkerEntity> findByTrustEntityAndAccount(TrustTrustEntity trustEntity, String account);

    List<TrustSpecialWorkerEntity> findByTrustEntity(TrustTrustEntity trustEntity);

    List<TrustSpecialWorkerEntity> findByAccountBetween(String accountOne, String accountTwo);

    List<TrustSpecialWorkerEntity> findByAccountIn(List<String> accounts);

    List<TrustSpecialWorkerEntity> findByNameContaining(String pattern);

    Page<TrustSpecialWorkerEntity> findByTrustEntity(TrustTrustEntity trustEntity, Pageable pageable);

}
