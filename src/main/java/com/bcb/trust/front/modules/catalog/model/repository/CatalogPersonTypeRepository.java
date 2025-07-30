package com.bcb.trust.front.modules.catalog.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonTypeEntity;

@Repository
public interface CatalogPersonTypeRepository extends JpaRepository<CatalogPersonTypeEntity, Long> {
    public List<CatalogPersonTypeEntity> findByStatus(StatusEnum statusEnum);
}
