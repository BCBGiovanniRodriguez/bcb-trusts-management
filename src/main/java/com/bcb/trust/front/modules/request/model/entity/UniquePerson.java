package com.bcb.trust.front.modules.request.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bcb.trust.front.modules.request.model.entity.catalog.UniquePersonMembership;
import com.bcb.trust.front.modules.system.model.entity.CatalogPersonEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_unique_persons")
public class UniquePerson {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long uniquePersonId;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private CatalogPersonEntity personEntity;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "uniquePersonEntity")
    List<UniquePersonMembership> uniquePersonMembershipList;

    public UniquePerson() {
    }

    public Long getUniquePersonId() {
        return uniquePersonId;
    }

    public void setUniquePersonId(Long uniquePersonId) {
        this.uniquePersonId = uniquePersonId;
    }

    public CatalogPersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(CatalogPersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("uniquePersonId", this.uniquePersonId);
        map.put("personEntity", this.personEntity.toMap());
        map.put("created", this.createdAt);

        return map;
    }
}
