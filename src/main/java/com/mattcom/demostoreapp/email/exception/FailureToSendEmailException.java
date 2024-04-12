package com.mattcom.demostoreapp.email.exception;

public class FailureToSendEmailException extends RuntimeException {

    public FailureToSendEmailException(String message) {
        super(message);
    }
}
