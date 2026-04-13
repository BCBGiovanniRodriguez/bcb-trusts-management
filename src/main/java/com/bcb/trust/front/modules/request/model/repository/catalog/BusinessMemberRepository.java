package com.bcb.trust.front.modules.request.model.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.request.model.entity.catalog.BusinessMemberEntity;

@Repository
public interface BusinessMemberRepository extends JpaRepository<BusinessMemberEntity, Long> {

}
