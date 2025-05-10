package com.bcb.trust.front.entity.enums;

public enum ProcessDetailStateEnum {

    PROCESSED(1, "Procesado"),
    ERROR(2, "Error");

    private final int intValue;
    private final String stringValue;

    ProcessDetailStateEnum(int intValue, String stringValue) {
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
