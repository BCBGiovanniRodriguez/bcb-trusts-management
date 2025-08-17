package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.bcb.trust.front.modules.catalog.model.entity.CatalogPersonEntity;
import com.bcb.trust.front.modules.common.model.CommonEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_users")
public class SystemUserEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private SystemProfileEntity profile;

    private String nickname;

    private String access;

    private String email;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "PersonId", referencedColumnName = "PersonId")
    private CatalogPersonEntity person;

    private Integer status;

    private LocalDateTime created;

    public SystemUserEntity() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SystemProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(SystemProfileEntity profile) {
        this.profile = profile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CatalogPersonEntity getPerson() {
        return person;
    }

    public void setPerson(CatalogPersonEntity person) {
        this.person = person;
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
        return "SystemUser [userId=" + userId + ", profile=" + profile + ", nickname=" + nickname + ", access=" + access
                + ", email=" + email + ", person=" + person + ", status=" + status + ", created=" + created + "]";
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("SystemUserEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }
    
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", this.userId);
        map.put("nickname", this.nickname);
        map.put("email", this.email);
        map.put("profile", this.profile.toMap());
        map.put("person", this.person.toMap());
        map.put("status", this.status);
        map.put("created", this.created);

        return map;
    }
}
