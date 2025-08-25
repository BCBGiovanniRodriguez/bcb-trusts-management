package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;

@Repository
public interface TrustTrustRepository extends JpaRepository<TrustTrustEntity, Long> {

    List<TrustTrustEntity> findByStatus(Integer status);

    List<TrustTrustEntity> findByState(Integer state);

    List<TrustTrustEntity> findByStateAndStatus(Integer state, Integer status);
    
}
