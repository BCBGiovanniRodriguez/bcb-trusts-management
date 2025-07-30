package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.model.trusts.enums.StatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trust_types")
public class TrustTrustTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustTypeId;

    private String name;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private StatusEnum status;

    private LocalDateTime created;

    public TrustTrustTypeEntity() {
    }

    public Long getTrustTypeId() {
        return trustTypeId;
    }

    public void setTrustTypeId(Long trustTypeId) {
        this.trustTypeId = trustTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "TrustTrustTypeEntity [trustTypeId=" + trustTypeId + ", name=" + name + ", description=" + description
                + ", status=" + status + ", created=" + created + "]";
    }    

}
