package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "system_profiles")
public class SystemProfileEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Integer members;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private LocalDateTime created;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "system_profile_permissions",
        joinColumns = @JoinColumn(name = "profileId"),
        inverseJoinColumns = @JoinColumn(name = "permissionId")
    )
    private Set<SystemPermissionEntity> permissions;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<SystemUserEntity> users;

    public SystemProfileEntity() {
        this.permissions = new HashSet<>();
        this.users = new ArrayList<>();
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
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
        return "SystemProfileEntity [profileId=" + profileId + ", name=" + name + ", status=" + status + ", created="
                + created + "]";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("profileId", this.profileId);
        map.put("name", this.name);
        map.put("members", this.members);
        map.put("status", this.status);
        map.put("created", this.created);

        return map;
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("SystemProfileEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    public Set<SystemPermissionEntity> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(Set<SystemPermissionEntity> permissions) {
        this.permissions = permissions;
    }

}
