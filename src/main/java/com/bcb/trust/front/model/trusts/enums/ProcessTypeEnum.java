package com.bcb.trust.front.model.trusts.enums;

public enum ProcessTypeEnum {
    
    MASSIVE_REPORT_GENERATION(1, "Generación Masiva de Reportes");

    private final int intValue;

    private final String stringValue;

    ProcessTypeEnum (int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public String getStrigValue() {
        return this.stringValue;
    }
}
