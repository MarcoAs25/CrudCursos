package com.marcoas.crudCursos.controller.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public final ResponseEntity<ErrorDetails> handleResourceNotFoundException(DataIntegrityViolationException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Esta ação não pode ser concluída pois este item está vinculado a outro.");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public final ResponseEntity<ErrorDetails> handleResourceNotFoundException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public final ResponseEntity<ErrorDetails> handleResourceNotFoundException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("Um erro inesperado ocorreu.");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiError.class)
    @ResponseBody
    public final ResponseEntity<ErrorDetails> handleResourceNotFoundException(ApiError ex) {
        return new ResponseEntity<>(new ErrorDetails(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
