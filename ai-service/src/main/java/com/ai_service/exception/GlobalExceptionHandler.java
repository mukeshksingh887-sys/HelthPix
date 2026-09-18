package com.ai_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException exception) {

        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),

                                "status",
                                400,

                                "message",
                                "Invalid request"
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            Exception exception) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),

                                "status",
                                500,

                                "message",
                                "AI service is temporarily unavailable"
                        )
                );
    }
}