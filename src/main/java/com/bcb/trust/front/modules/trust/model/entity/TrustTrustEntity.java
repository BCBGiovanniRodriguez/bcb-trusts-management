package com.bcb.trust.front.modules.trust.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trust_trusts")
public class TrustTrustEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustId;

    private Integer number;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type", referencedColumnName = "trustTypeId")
    private TrustTrustTypeEntity typeEntity;

    private Integer state;

    private Integer status;

    private LocalDateTime created;

    public TrustTrustEntity() {
    }

    public Long getTrustId() {
        return trustId;
    }

    public void setTrustId(Long trustId) {
        this.trustId = trustId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrustTrustTypeEntity getTypeEntity() {
        return typeEntity;
    }

    public void setTypeEntity(TrustTrustTypeEntity typeEntity) {
        this.typeEntity = typeEntity;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
        return "TrustTrustEntity [trustId=" + trustId + ", number=" + number + ", name=" + name + ", typeEntity="
                + typeEntity + ", state=" + state + ", status=" + status + ", created=" + created + "]";
    }
    
}
