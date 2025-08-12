package com.bcb.trust.front.modules.common.model;

public abstract class CommonEntity {

    public static final Integer STATUS_ENABLED = 1;

    public static final Integer STATUS_DISABLED = 2;

    public static final Integer SIMPLE_OPTION_YES = 1;

    public static final Integer SIMPLE_OPTION_NO = 2;

    public static String[] simpleOptions = {"Seleccione Opción", "Si", "No"};

    public static String[] statuses = {"Seleccione Opción", "Habilitado", "Deshabilitado"};

    public abstract String getStatusAsString() throws Exception;
}
