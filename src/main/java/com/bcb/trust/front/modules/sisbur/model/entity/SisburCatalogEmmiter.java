package com.bcb.trust.front.modules.sisbur.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sisbur_catalog_emmiters")
public class SisburCatalogEmmiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emmiterId;

    private String name;

    private String description;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "emmiter")
    private List<SisburCatalogInstrument> instruments;

    public SisburCatalogEmmiter() {
        this.instruments = new ArrayList<>();
    }

    public Long getEmmiterId() {
        return emmiterId;
    }

    public void setEmmiterId(Long emmiterId) {
        this.emmiterId = emmiterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
