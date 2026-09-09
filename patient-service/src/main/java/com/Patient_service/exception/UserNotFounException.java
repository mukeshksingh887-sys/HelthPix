package com.Patient_service.exception;

public class UserNotFounException extends RuntimeException {
    public UserNotFounException(String message) {
        super(message);
    }
}
