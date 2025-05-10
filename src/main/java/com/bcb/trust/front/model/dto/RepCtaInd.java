package com.bcb.trust.front.model.dto;

public class RepCtaInd {

    private String rciFecha;
    
    private String rciNomInvers;
    
    private Double rciDepositos;
    
    private Double rciRetiros;
    
    private Double rciSaldo;

    private Integer rciSecuencial;

    public RepCtaInd() {
    }

    public String getRciFecha() {
        return rciFecha;
    }

    public void setRciFecha(String rciFecha) {
        this.rciFecha = rciFecha;
    }

    public String getRciNomInvers() {
        return rciNomInvers;
    }

    public void setRciNomInvers(String rciNomInvers) {
        this.rciNomInvers = rciNomInvers;
    }

    public Double getRciDepositos() {
        return rciDepositos;
    }

    public void setRciDepositos(Double rciDepositos) {
        this.rciDepositos = rciDepositos;
    }

    public Double getRciRetiros() {
        return rciRetiros;
    }

    public void setRciRetiros(Double rciRetiros) {
        this.rciRetiros = rciRetiros;
    }

    public Double getRciSaldo() {
        return rciSaldo;
    }

    public void setRciSaldo(Double rciSaldo) {
        this.rciSaldo = rciSaldo;
    }

    public Integer getRciSecuencial() {
        return rciSecuencial;
    }

    public void setRciSecuencial(Integer rciSecuencial) {
        this.rciSecuencial = rciSecuencial;
    }
}
