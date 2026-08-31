package com.doctor_service.exception;

public class ResourceNotFoundException extends  RuntimeException{
    public ResourceNotFoundException(String messag) {
        super(messag);
    }
}
