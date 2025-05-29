package com.mattcom.demostoreapp.order.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(Integer id) {
        super("Could not find order with id" + id);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
