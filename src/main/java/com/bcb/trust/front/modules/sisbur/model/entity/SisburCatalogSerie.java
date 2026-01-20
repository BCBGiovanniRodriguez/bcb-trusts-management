package com.bcb.trust.front.modules.sisbur.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sisbur_catalog_series")
public class SisburCatalogSerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serieId;

    private String name;

    private String description;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "serie")
    private List<SisburCatalogInstrument> instruments;

    public SisburCatalogSerie() {
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
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
