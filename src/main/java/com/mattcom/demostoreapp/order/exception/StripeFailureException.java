package com.mattcom.demostoreapp.order.exception;

public class StripeFailureException extends RuntimeException{
    public StripeFailureException(String message) {
        super(message);
    }
}
