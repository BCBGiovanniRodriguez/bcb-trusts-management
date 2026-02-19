package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_user_activities")
public class SystemUserActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userActivityId;

    @ManyToOne
    @JoinColumn(name = "resourceId", referencedColumnName = "resourceId")
    private CatalogResourceEntity resourceEntity;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    public SystemUserActivityEntity() {
    }

    public Long getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Long userActivityId) {
        this.userActivityId = userActivityId;
    }

    public CatalogResourceEntity getResourceEntity() {
        return resourceEntity;
    }

    public void setResourceEntity(CatalogResourceEntity resourceEntity) {
        this.resourceEntity = resourceEntity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "SystemUserActivityEntity [userActivityId=" + userActivityId + ", resourceEntity=" + resourceEntity
                + ", status=" + status + ", created=" + createdAt + "]";
    }

}
