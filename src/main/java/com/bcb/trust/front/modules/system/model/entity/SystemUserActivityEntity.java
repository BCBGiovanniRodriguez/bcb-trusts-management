package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import com.bcb.trust.front.model.trusts.entity.system.SystemPermission;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_user_activites")
public class SystemUserActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userActivityId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PermissionId", referencedColumnName = "PermissionId")
    private SystemPermission permission;

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

    public SystemPermission getPermission() {
        return permission;
    }

    public void setPermission(SystemPermission permission) {
        this.permission = permission;
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
        return "SystemUserActivity [userActivityId=" + userActivityId + ", permission=" + permission + ", status="
                + status + ", created=" + created + "]";
    }

}
