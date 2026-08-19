package com.Patient_service.exception;

public class PatientNotFoundException extends  RuntimeException{

    public PatientNotFoundException(String message) {

        super(message);
    }
}
