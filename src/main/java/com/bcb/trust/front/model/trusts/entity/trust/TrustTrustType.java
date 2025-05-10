package com.bcb.trust.front.model.trusts.entity.trust;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TrustTrustType")
public class TrustTrustType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer TrustTypeId;

    private String Name;

    private String Code;

    private Integer Status;

    private LocalDateTime Created;

    public TrustTrustType() {
    }

    public Integer getTrustTypeId() {
        return TrustTypeId;
    }

    public void setTrustTypeId(Integer trustTypeId) {
        TrustTypeId = trustTypeId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
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
        return "TrustType [TrustTypeId=" + TrustTypeId + ", Name=" + Name + ", Code=" + Code + ", Status=" + Status
                + ", Created=" + Created + "]";
    }

}
