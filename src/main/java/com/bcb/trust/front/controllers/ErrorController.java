package com.bcb.trust.front.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleError(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Server error", ex.getLocalizedMessage(), 500);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
