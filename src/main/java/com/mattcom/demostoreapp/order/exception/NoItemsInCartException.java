package com.mattcom.demostoreapp.order.exception;

public class NoItemsInCartException extends RuntimeException{
    public NoItemsInCartException(String message) {
        super(message);
    }
}
