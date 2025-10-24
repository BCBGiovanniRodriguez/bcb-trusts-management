package com.bcb.trust.front.modules.trust.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustQuarterEntity;

@Repository
public interface TrustQuarterRepository extends JpaRepository<TrustQuarterEntity, Long> {

}
