package com.doctor_service.exception;

public class LicenseNumberAlreadyExistsException extends RuntimeException {
    public LicenseNumberAlreadyExistsException(String message) {
        super(message);
    }
}
