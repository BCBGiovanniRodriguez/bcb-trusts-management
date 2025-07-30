package com.bcb.trust.front.modules.trust.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustTypeEntity;

@Repository
public interface TrustTrustTypeRepository extends JpaRepository<TrustTrustTypeEntity, Long> {

    public List<TrustTrustTypeEntity> findByStatus(StatusEnum status);
}
