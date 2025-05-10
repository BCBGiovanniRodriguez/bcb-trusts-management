package com.bcb.trust.front.entity.enums;

public enum StatusEnum {

    ENABLED(1, "Activo"),
    DISABLED(2, "Inactivo");

    private final int intValue;

    private final String stringValue;

    StatusEnum(int intValue, String stringValue) {
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
