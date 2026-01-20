package com.bcb.trust.front.modules.system.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.SystemResourceEntity;

@Repository
public interface SystemResourceRepository extends JpaRepository<SystemResourceEntity, Long> {

    SystemResourceEntity findOneByElement(String element);

    SystemResourceEntity findOneByCode(String code);

    // List<SystemResourceEntity> findByModuleAndStatus(Integer module, Integer
    // status);

    // List<SystemResourceEntity> findByStatus(Integer status);

    List<SystemResourceEntity> findByModule(Integer module);
}
