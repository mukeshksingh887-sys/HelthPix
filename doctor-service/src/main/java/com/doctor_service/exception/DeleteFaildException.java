package com.doctor_service.exception;

public class DeleteFaildException extends RuntimeException {
    public DeleteFaildException(String message) {
        super(message);
    }
}
