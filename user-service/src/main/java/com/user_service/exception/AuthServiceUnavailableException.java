package com.user_service.exception;

public class AuthServiceUnavailableException extends RuntimeException {

    public AuthServiceUnavailableException(String message) {
        super(message);
    }
}
