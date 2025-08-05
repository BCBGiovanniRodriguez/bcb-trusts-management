package com.bcb.trust.front.modules.common.model;

public abstract class CommonEntity {

    public static String[] statuses = {"Seleccione Opción", "Habilitado", "Deshabilitado"};

    public abstract String getStatusAsString() throws Exception;
}
