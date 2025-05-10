package com.bcb.trust.front.model.trusts.entity.system;

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
@Table(name = "SystemUserActivity")
public class SystemUserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long UserActivityId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PermissionId", referencedColumnName = "PermissionId")
    private SystemPermission SystemPermission;

    private Integer Status;

    private LocalDateTime Created;

    public SystemUserActivity() {
    }

    public Long getUserActivityId() {
        return UserActivityId;
    }

    public void setUserActivityId(Long userActivityId) {
        UserActivityId = userActivityId;
    }

    public SystemPermission getSystemPermission() {
        return SystemPermission;
    }

    public void setSystemPermission(SystemPermission systemPermission) {
        SystemPermission = systemPermission;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public LocalDateTime getCreated() {
        return Created;
    }

    public void setCreated(LocalDateTime created) {
        Created = created;
    }

    @Override
    public String toString() {
        return "SystemUserActivity [UserActivityId=" + UserActivityId + ", SystemPermission=" + SystemPermission
                + ", Status=" + Status + ", Created=" + Created + "]";
    }
}
