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
@Table(name = "system_configuration_user_profiles")
public class ConfigurationUserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CatalogUserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private CatalogProfileEntity profileEntity;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer active;

    private LocalDateTime assignedAt;

    @ManyToOne
    @JoinColumn(name = "assigned_by", nullable = false)
    private CatalogUserEntity assignedBy;

    @ManyToOne
    @JoinColumn(name = "unassigned_by", nullable = false)
    private CatalogUserEntity unassignedBy;

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public CatalogUserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(CatalogUserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public CatalogProfileEntity getProfileEntity() {
        return profileEntity;
    }

    public void setProfileEntity(CatalogProfileEntity profileEntity) {
        this.profileEntity = profileEntity;
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

    public CatalogUserEntity getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(CatalogUserEntity assignedBy) {
        this.assignedBy = assignedBy;
    }
    
    public CatalogUserEntity getUnassignedBy() {
        return unassignedBy;
    }

    public void setUnassignedBy(CatalogUserEntity unassignedBy) {
        this.unassignedBy = unassignedBy;
    }
}
