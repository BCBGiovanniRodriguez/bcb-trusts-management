package com.bcb.trust.front.modules.common.enums;

public enum CommonPersonTypeEnum {

    PERSON(1, "física"),
    ENTERPRISE(2, "moral");

    private final int intValue;

    private final String stringValue;

    CommonPersonTypeEnum (int intValue, String stringValue) {
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
