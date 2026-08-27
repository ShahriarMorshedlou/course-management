package com.shah.course_management.exception;


import com.shah.course_management.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        ex.getBindingResult()
                .getGlobalErrors()
                .forEach(error ->
                        errors.put(error.getObjectName(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                400,
                "Validation failed",
                errors
        );

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
}
