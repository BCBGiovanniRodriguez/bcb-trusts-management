package com.bcb.trust.front.model.trusts.entity.system;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SystemProfile")
public class SystemProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProfileId;

    private String Name;

    private Integer Status;

    private LocalDate Created;

    public SystemProfile() {
    }

    public Long getProfileId() {
        return ProfileId;
    }

    public void setProfileId(Long profileId) {
        ProfileId = profileId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public LocalDate getCreated() {
        return Created;
    }

    public void setCreated(LocalDate created) {
        Created = created;
    }

    @Override
    public String toString() {
        return "Profile [ProfileId=" + ProfileId + ", Name=" + Name + ", Status=" + Status + ", Created=" + Created
                + "]";
    }
    
}
