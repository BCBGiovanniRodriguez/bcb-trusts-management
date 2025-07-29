package com.bcb.trust.front.model.trusts.enums;

public enum StatusEnum {

    DISABLED (0, "deshabilitado"),
    ENABLED (1, "habilitado");

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
