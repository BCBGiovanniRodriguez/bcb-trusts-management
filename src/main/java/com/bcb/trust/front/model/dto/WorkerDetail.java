package com.bcb.trust.front.model.dto;

import java.time.LocalDate;

public class WorkerDetail {

    private String datClave;

    private Integer datContrato;

    private String datDato;

    private String datDescripcion;

    private String datEstatus;

    private LocalDate datFechaAlta;

    private LocalDate datFechaBaja;

    private LocalDate datFecUltMod;

    private Integer datId;

    private Integer datNivel;

    private Integer datParentId;

    public WorkerDetail() {
    }

    public String getDatClave() {
        return datClave;
    }

    public void setDatClave(String datClave) {
        this.datClave = datClave;
    }

    public Integer getDatContrato() {
        return datContrato;
    }

    public void setDatContrato(Integer datContrato) {
        this.datContrato = datContrato;
    }

    public String getDatDato() {
        return datDato;
    }

    public void setDatDato(String datDato) {
        this.datDato = datDato;
    }

    public String getDatDescripcion() {
        return datDescripcion;
    }

    public void setDatDescripcion(String datDescripcion) {
        this.datDescripcion = datDescripcion;
    }

    public String getDatEstatus() {
        return datEstatus;
    }

    public void setDatEstatus(String datEstatus) {
        this.datEstatus = datEstatus;
    }

    public LocalDate getDatFechaAlta() {
        return datFechaAlta;
    }

    public void setDatFechaAlta(LocalDate datFechaAlta) {
        this.datFechaAlta = datFechaAlta;
    }

    public LocalDate getDatFechaBaja() {
        return datFechaBaja;
    }

    public void setDatFechaBaja(LocalDate datFechaBaja) {
        this.datFechaBaja = datFechaBaja;
    }

    public LocalDate getDatFecUltMod() {
        return datFecUltMod;
    }

    public void setDatFecUltMod(LocalDate datFecUltMod) {
        this.datFecUltMod = datFecUltMod;
    }

    public Integer getDatId() {
        return datId;
    }

    public void setDatId(Integer datId) {
        this.datId = datId;
    }

    public Integer getDatNivel() {
        return datNivel;
    }

    public void setDatNivel(Integer datNivel) {
        this.datNivel = datNivel;
    }

    public Integer getDatParentId() {
        return datParentId;
    }

    public void setDatParentId(Integer datParentId) {
        this.datParentId = datParentId;
    }

    @Override
    public String toString() {
        return "WorkerDetail [datClave=" + datClave + ", datContrato=" + datContrato + ", datDato=" + datDato
                + ", datDescripcion=" + datDescripcion + ", datEstatus=" + datEstatus + ", datFechaAlta=" + datFechaAlta
                + ", datFechaBaja=" + datFechaBaja + ", datFecUltMod=" + datFecUltMod + ", datId=" + datId
                + ", datNivel=" + datNivel + ", datParentId=" + datParentId + "]";
    }
}
