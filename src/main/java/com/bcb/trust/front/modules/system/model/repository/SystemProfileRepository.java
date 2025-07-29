package com.bcb.trust.front.modules.system.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.SystemProfileEntity;

@Repository
public interface SystemProfileRepository extends JpaRepository<SystemProfileEntity, Long> {

}
