package com.doctor_service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> handleServiceUnavailable(
            ServiceUnavailableException ex) {

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "message", ex.getMessage()
                ));
    }


    @ExceptionHandler(
            LicenseNumberAlreadyExistsException.class)
    public ResponseEntity<String> handleLicenseNumberExists(
            LicenseNumberAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourseNotFoundExcepiotn(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 503,
                        "message", ex.getMessage()
                ));
    }


    @ExceptionHandler(
            DeleteFaildException.class)
    public ResponseEntity<String> handleDeleteFailedException(
            DeleteFaildException ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }



}