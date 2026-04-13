package com.bcb.trust.front.modules.request.model.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.request.model.entity.catalog.ResourceOriginEntity;

@Repository
public interface ResourceOriginRepository extends JpaRepository<ResourceOriginEntity, Long> {

}
