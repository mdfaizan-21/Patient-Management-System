package com.pm.patientservice.exception.custom;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String s) {
        super(s);
    }
}
