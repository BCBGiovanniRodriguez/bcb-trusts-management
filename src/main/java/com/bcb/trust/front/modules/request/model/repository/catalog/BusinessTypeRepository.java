package com.bcb.trust.front.modules.request.model.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.request.model.entity.catalog.BusinessTypeEntity;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessTypeEntity, Long> {

    List<BusinessTypeEntity> findByStatus(Integer status);

}
