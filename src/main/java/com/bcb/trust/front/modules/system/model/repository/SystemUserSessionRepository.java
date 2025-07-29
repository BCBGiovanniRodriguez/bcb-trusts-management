package com.bcb.trust.front.modules.system.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.SystemUserSessionEntity;

@Repository
public interface SystemUserSessionRepository extends JpaRepository<SystemUserSessionEntity, Long> {

}
