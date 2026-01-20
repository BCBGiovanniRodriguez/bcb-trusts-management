package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_profiles")
public class SystemProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Integer members;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ConfigurationProfileResourceEntity> profileResourceList;

    public List<ConfigurationProfileResourceEntity> getProfileResourceList() {
        return profileResourceList;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "SystemProfileEntity [profileId=" + profileId + ", name=" + name + ", description=" + members
                + ", createdAt=" + createdAt + "]";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("profileId", this.profileId);
        map.put("name", this.name);
        map.put("members", this.members);
        map.put("created", this.createdAt);

        return map;
    }

}
