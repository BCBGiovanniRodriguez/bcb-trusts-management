package com.bcb.trust.front.model.trusts.entity.catalog;

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
@Table(name = "catalog_trust_type")
public class TrustType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trustTypeId;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private StatusEnum status;

    private LocalDateTime created;

    public TrustType() {
    }

    public long getTrustTypeId() {
        return trustTypeId;
    }

    public void setTrustTypeId(long trustTypeId) {
        this.trustTypeId = trustTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "TrustType [trustTypeId=" + trustTypeId + ", name=" + name + ", status=" + status + ", created="
                + created + "]";
    }

}
