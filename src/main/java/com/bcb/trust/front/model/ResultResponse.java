package com.bcb.trust.front.model;

public class ResultResponse<T> {

    private final boolean isSuccess;
    
    private final T value;
    
    private final String errorMessage;

    private ResultResponse(boolean isSuccess, T value, String errorMessage) {
        this.isSuccess = isSuccess;
        this.value = value;
        this.errorMessage = errorMessage;
    }

    public static <T> ResultResponse<T> success(T value) {
        return new ResultResponse<>(true, value, null);
    }

    public static <T> ResultResponse<T> failure(String errorMessage) {
        return new ResultResponse<>(false, null, errorMessage);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public T getValue() {
        if (!isSuccess) {
            throw new IllegalStateException("Surgió un error, no se puede obtener resultado.");
        }
        
        return value;
    }

    public String getErrorMessage() {
        if (isSuccess) {
            throw new IllegalStateException("Resultado correcto, no hay errores.");
        }

        return errorMessage;
    }
}