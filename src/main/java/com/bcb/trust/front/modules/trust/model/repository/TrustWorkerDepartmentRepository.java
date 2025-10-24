package com.bcb.trust.front.modules.trust.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.trust.model.entity.TrustWorkerDepartmentEntity;

@Repository
public interface TrustWorkerDepartmentRepository extends JpaRepository<TrustWorkerDepartmentEntity, Long> {

    Optional<TrustWorkerDepartmentEntity> findByNumber(String number);

}
