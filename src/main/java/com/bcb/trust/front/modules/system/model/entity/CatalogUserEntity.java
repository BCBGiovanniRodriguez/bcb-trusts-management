package com.bcb.trust.front.modules.system.model.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bcb.trust.front.modules.common.model.CommonEntity;
import com.bcb.trust.front.modules.request.model.entity.RequestRequestEntity;
import com.bcb.trust.front.modules.trust.model.entity.TrustCatalogMovementType;
import com.bcb.trust.front.modules.trust.model.entity.TrustTrustEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_catalog_users")
public class CatalogUserEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String nickname;

    private String access;

    private String email;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "personId", referencedColumnName = "personId")
    private CatalogPersonEntity person;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "registeredBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestRequestEntity> requestList;

    @OneToMany(mappedBy = "registeredBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustTrustEntity> trustList;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustCatalogMovementType> movementTypeList;

    @OneToMany(mappedBy = "assignedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfigurationProfileResourceEntity> profileResourceList;

    public CatalogUserEntity() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime created) {
        this.createdAt = created;
    }

    @Override
    public String toString() {
        return "CatalogUserEntity [userId=" + userId + ", nickname=" + nickname + ", access=" + access
                + ", email=" + email + ", person=" + person + ", status=" + status + ", created=" + createdAt + "]";
    }

    @Override
    public String getStatusAsString() throws Exception {
        if (this.status < 0 || (this.status > CommonEntity.statuses.length)) {
            throw new Exception("CatalogUserEntity::getStatusAsString::Valor de estatus fuera del rango");
        }

        return CommonEntity.statuses[this.status];
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", this.userId);
        map.put("nickname", this.nickname);
        map.put("email", this.email);
        map.put("person", this.person.toMap());
        map.put("status", this.status);
        map.put("created_at", this.createdAt);

        return map;
    }

    public List<RequestRequestEntity> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestRequestEntity> requestList) {
        this.requestList = requestList;
    }

    public List<TrustTrustEntity> getTrustList() {
        return trustList;
    }

    public void setTrustList(List<TrustTrustEntity> trustList) {
        this.trustList = trustList;
    }
}
