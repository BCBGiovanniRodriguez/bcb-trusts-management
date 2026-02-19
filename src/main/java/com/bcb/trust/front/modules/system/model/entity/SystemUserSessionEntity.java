package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_user_sessions")
public class SystemUserSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSessionId;

    private String name;

    private String address;

    private String userAgent;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CatalogUserEntity userEntity;

    public SystemUserSessionEntity() {
    }

    public Long getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(Long userSessionId) {
        this.userSessionId = userSessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "CatalogUserSessionEntity [userSessionId=" + userSessionId + ", name=" + name + ", address=" + address
                + ", userAgent=" + userAgent + ", created=" + createdAt + "]";
    }

    public CatalogUserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(CatalogUserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
