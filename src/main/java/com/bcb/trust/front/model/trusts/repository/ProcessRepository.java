package com.bcb.trust.front.model.trusts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.model.trusts.entity.ProcessEntity;

@Repository
public interface ProcessRepository extends JpaRepository<ProcessEntity, Integer> {

}
