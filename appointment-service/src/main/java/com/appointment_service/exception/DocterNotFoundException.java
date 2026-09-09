package com.appointment_service.exception;

public class DocterNotFoundException extends RuntimeException {
    public DocterNotFoundException(String message) {
        super(message);
    }
}
