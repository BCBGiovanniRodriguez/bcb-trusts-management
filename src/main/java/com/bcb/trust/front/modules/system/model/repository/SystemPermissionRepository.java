package com.bcb.trust.front.modules.system.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.SystemPermissionEntity;

@Repository
public interface SystemPermissionRepository extends JpaRepository<SystemPermissionEntity, Long> {
    
    SystemPermissionEntity findOneByName(String name);

    SystemPermissionEntity findOneByCode(String code);

    List<SystemPermissionEntity> findByModuleAndStatus(Integer module, Integer status);

    List<SystemPermissionEntity> findByStatus(Integer status);

    List<SystemPermissionEntity> findByModule(Integer module);
}
