package com.bcb.trust.front.model.trusts.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;
import com.bcb.trust.front.model.trusts.entity.catalog.PersonType;

@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Long> {
    public List<PersonType> findByStatus(StatusEnum statusEnum);
}
