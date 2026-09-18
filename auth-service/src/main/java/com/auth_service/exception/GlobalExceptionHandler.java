package com.auth_service.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> ResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        Map.of(
                                "timestamp", LocalDateTime.now(),
                                "status", 401,
                                "message", ex.getMessage()
                        )
                );
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


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExists(
            UserAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        Map.of(
                                "timestamp", LocalDateTime.now(),
                                "status", 503,
                                "message", ex.getMessage()
                        )

                );
    }


}