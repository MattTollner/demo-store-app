package com.mattcom.demostoreapp.order.exception;

public class IncorrectUserToOrderException extends RuntimeException{
    public IncorrectUserToOrderException() {
        super("User logged in is not the same as who made the order");
    }
}
