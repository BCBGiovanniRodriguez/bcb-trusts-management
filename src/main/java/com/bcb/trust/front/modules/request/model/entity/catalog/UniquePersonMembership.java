package com.bcb.trust.front.modules.request.model.entity.catalog;

import java.time.LocalDateTime;

import com.bcb.trust.front.modules.request.model.entity.UniquePerson;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "request_unique_person_membership")
public class UniquePersonMembership {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long uniquePersonMembershipId;

    @ManyToOne
    @JoinColumn(name = "unique_person_id")
    private UniquePerson uniquePersonEntity;

    @ManyToOne
    @JoinColumn(name = "business_member_id")
    private BusinessMemberEntity businessMemberEntity;

    @Column(columnDefinition = "TINYINT")
    private Integer type;

    private Long rcNumber;

    private LocalDateTime createdAt;

    public UniquePersonMembership() {
    }

    public Long getUniquePersonMembershipId() {
        return uniquePersonMembershipId;
    }

    public void setUniquePersonMembershipId(Long uniquePersonMembershipId) {
        this.uniquePersonMembershipId = uniquePersonMembershipId;
    }

    public UniquePerson getUniquePersonEntity() {
        return uniquePersonEntity;
    }

    public void setUniquePersonEntity(UniquePerson uniquePersonEntity) {
        this.uniquePersonEntity = uniquePersonEntity;
    }

    public BusinessMemberEntity getBusinessMemberEntity() {
        return businessMemberEntity;
    }

    public void setBusinessMemberEntity(BusinessMemberEntity businessMemberEntity) {
        this.businessMemberEntity = businessMemberEntity;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRcNumber() {
        return rcNumber;
    }

    public void setRcNumber(Long rcNumber) {
        this.rcNumber = rcNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
