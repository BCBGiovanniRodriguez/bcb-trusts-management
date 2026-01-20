package com.bcb.trust.front.modules.sisbur.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sisbur_catalog_instruments")
public class SisburCatalogInstrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instrumentId;

    @ManyToOne
    @JoinColumn(name = "valueTypeId", nullable = false)
    private SisburCatalogValueType valueType;

    @ManyToOne
    @JoinColumn(name = "emmiterId", nullable = false)
    private SisburCatalogEmmiter emmiter;

    @ManyToOne
    @JoinColumn(name = "serieId", nullable = false)
    private SisburCatalogSerie serie;

    private LocalDateTime createdAt;

    public SisburCatalogInstrument() {
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public SisburCatalogValueType getValueType() {
        return valueType;
    }

    public void setValueType(SisburCatalogValueType valueType) {
        this.valueType = valueType;
    }

    public SisburCatalogEmmiter getEmmiter() {
        return emmiter;
    }

    public void setEmmiter(SisburCatalogEmmiter emmiter) {
        this.emmiter = emmiter;
    }

    public SisburCatalogSerie getSerie() {
        return serie;
    }

    public void setSerie(SisburCatalogSerie serie) {
        this.serie = serie;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
