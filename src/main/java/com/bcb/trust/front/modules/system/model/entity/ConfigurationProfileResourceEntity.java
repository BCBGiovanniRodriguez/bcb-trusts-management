package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_configuration_profile_resources")
public class ConfigurationProfileResourceEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long profileResourceId;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private SystemProfileEntity profile;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private SystemResourceEntity resource;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer permissionType;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer active;

    private LocalDateTime assignedAt;

    @ManyToOne
    @JoinColumn(name = "assigned_by", nullable = false)
    private SystemUserEntity assignedBy;

    public Long getProfileResourceId() {
        return profileResourceId;
    }

    public void setProfileResourceId(Long profileResourceId) {
        this.profileResourceId = profileResourceId;
    }

    public SystemProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(SystemProfileEntity profile) {
        this.profile = profile;
    }

    public SystemResourceEntity getResource() {
        return resource;
    }

    public void setResource(SystemResourceEntity resourceId) {
        this.resource = resourceId;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public SystemUserEntity getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(SystemUserEntity assignedBy) {
        this.assignedBy = assignedBy;
    }

}
