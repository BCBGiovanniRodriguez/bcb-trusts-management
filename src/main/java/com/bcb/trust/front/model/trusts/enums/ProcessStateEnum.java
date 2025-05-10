package com.bcb.trust.front.model.trusts.enums;

public enum ProcessStateEnum {
    
    REQUESTED (1, "Solicitado"),
    STARTED (2, "Iniciado"),
    PAUSED (3, "Pausado"),
    CANCELED (4, "Cancelado"),
    FINISHED (5, "Terminado"),
    ERROR (6, "Error");

    private final int intValue;

    private final String stringValue;
    
    ProcessStateEnum(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }
}
