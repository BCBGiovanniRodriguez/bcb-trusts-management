package com.bcb.trust.front.modules.request.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bcb.trust.front.modules.request.model.entity.UniquePerson;

@Repository
public interface UniquePersonRepository extends JpaRepository<UniquePerson, Long> {

    List<UniquePerson> findByPersonEntityFirstNameContaining(String name);

    List<UniquePerson> findByPersonEntitySecondNameContaining(String name);

    List<UniquePerson> findByPersonEntityLastNameContaining(String name);

    List<UniquePerson> findByPersonEntitySecondLastNameContaining(String name);

    List<UniquePerson> findByPersonEntityRfcContaining(String rfc);

}
