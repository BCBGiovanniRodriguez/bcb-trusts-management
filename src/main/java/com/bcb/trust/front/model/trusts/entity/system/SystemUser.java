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
    private Long UserId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ProfileId", referencedColumnName = "ProfileId")
    private SystemProfile Profile;

    private String Nickname;

    private String Access;

    private String Email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PersonId", referencedColumnName = "PersonId")
    private CatalogPerson Person;

    private Integer Status;

    private LocalDateTime Created;

    public SystemUser() {
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public SystemProfile getProfile() {
        return Profile;
    }

    public void setProfile(SystemProfile profile) {
        Profile = profile;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getAccess() {
        return Access;
    }

    public void setAccess(String access) {
        Access = access;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public CatalogPerson getPerson() {
        return Person;
    }

    public void setPerson(CatalogPerson person) {
        Person = person;
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
        return "User [UserId=" + UserId + ", Profile=" + Profile + ", Nickname=" + Nickname + ", Access=" + Access
                + ", Email=" + Email + ", Person=" + Person + ", Status=" + Status + ", Created=" + Created + "]";
    }
    
}
