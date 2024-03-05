package com.marcoas.crudCursos.controller.exception;


import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ApiError extends ResponseStatusException {
    private String message;
    public ApiError(String message) {
        super(HttpStatusCode.valueOf(500));
        this.message = message;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
