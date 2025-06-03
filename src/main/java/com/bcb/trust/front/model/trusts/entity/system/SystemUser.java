package com.bcb.trust.front.model.trusts.entity.system;

import java.time.LocalDateTime;

import com.bcb.trust.front.model.trusts.entity.catalog.CatalogPerson;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SystemUser")
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ProfileId", referencedColumnName = "ProfileId")
    private SystemProfile profile;

    private String nickname;

    private String access;

    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PersonId", referencedColumnName = "PersonId")
    private CatalogPerson person;

    private Integer status;

    private LocalDateTime created;

    public SystemUser() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SystemProfile getProfile() {
        return profile;
    }

    public void setProfile(SystemProfile profile) {
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

    public CatalogPerson getPerson() {
        return person;
    }

    public void setPerson(CatalogPerson person) {
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
    
}
