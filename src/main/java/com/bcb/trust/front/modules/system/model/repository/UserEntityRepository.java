package com.bcb.trust.front.modules.system.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.system.model.entity.CatalogUserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<CatalogUserEntity, Long> {

    CatalogUserEntity findByNickname(String nickname);

    Optional<CatalogUserEntity> findOneByEmail(String email);

    List<CatalogUserEntity> findByStatus(Integer status);
}
