package com.mattcom.demostoreapp.product.exception;

public class ProductNotFoundException extends RuntimeException{

    public static final long serialVersionUID = 1L;

    public ProductNotFoundException(String message) {
        super(message);
    }
}
