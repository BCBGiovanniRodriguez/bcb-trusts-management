package com.bcb.trust.front.modules.system.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.SystemUserEntity;

@Repository
public interface SystemUserEntityRepository extends JpaRepository<SystemUserEntity, Long> {

    SystemUserEntity findByNickname(String nickname);
}
