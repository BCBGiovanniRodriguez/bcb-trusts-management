package com.bcb.trust.front.modules.system.model.entity;

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
@Table(name = "system_user_activities")
public class SystemUserActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userActivityId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permissionId", referencedColumnName = "permissionId")
    private SystemPermissionEntity permissionEntity;

    private Integer status;

    private LocalDateTime created;

    public SystemUserActivityEntity() {
    }

    public Long getUserActivityId() {
        return userActivityId;
    }

    public void setUserActivityId(Long userActivityId) {
        this.userActivityId = userActivityId;
    }

    public SystemPermissionEntity getPermissionEntity() {
        return permissionEntity;
    }

    public void setPermissionEntity(SystemPermissionEntity permissionEntity) {
        this.permissionEntity = permissionEntity;
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
        return "SystemUserActivityEntity [userActivityId=" + userActivityId + ", permissionEntity=" + permissionEntity
                + ", status=" + status + ", created=" + created + "]";
    }

}
