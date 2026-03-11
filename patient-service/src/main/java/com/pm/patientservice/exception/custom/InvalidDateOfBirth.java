package com.pm.patientservice.exception.custom;

public class InvalidDateOfBirth extends RuntimeException {
    public InvalidDateOfBirth(String message) {
        super(message);
    }
}
