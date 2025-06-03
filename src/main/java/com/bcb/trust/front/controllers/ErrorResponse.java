package com.bcb.trust.front.controllers;

import lombok.Data;

@Data
public class ErrorResponse {
    private String title;
    private String message;
    private int status;

    public ErrorResponse(String title, String message, int status) {
        this.title = title;
        this.message = message;
        this.status = status;
    }
}
