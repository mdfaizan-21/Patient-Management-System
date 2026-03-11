package com.pm.patientservice.exception;

import com.pm.patientservice.exception.custom.EmailAlreadyExistException;
import com.pm.patientservice.exception.custom.InvalidDateOfBirth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionalHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionalHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.
            getBindingResult().
            getFieldErrors().
            forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {
        Map<String, String> errors = new HashMap<>();

        //for server side acknowledgement
        log.warn("Email already exist :- "+ex.getMessage());

        //for client side acknowledgement
        errors.put("email", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateOfBirth.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateOfBirth(InvalidDateOfBirth ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("dateOfBirth", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
