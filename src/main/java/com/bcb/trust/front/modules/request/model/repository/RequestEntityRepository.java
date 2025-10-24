package com.bcb.trust.front.modules.request.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;


@Repository
public interface RequestEntityRepository extends JpaRepository<RequestRequestEntity, Long> {

    RequestRequestEntity findFirstByOrderByNumberDesc();

    Optional<RequestRequestEntity> findOneByNumber(Integer number);

}
