package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustSpecialSelectedWorkerEntity;

@Repository
public interface TrustSpecialSelectedWorkerRepository extends JpaRepository<TrustSpecialSelectedWorkerEntity, Long> {

    Long countByContractNumber(Integer contractNumber);

    List<TrustSpecialSelectedWorkerEntity> findTop500ByContractNumberOrderByAccountDesc(Integer contractNumber);

    Page<TrustSpecialSelectedWorkerEntity> findByContractNumber(Integer contractNumber, Pageable pageable);

}
