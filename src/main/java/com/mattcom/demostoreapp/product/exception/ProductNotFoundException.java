package com.mattcom.demostoreapp.product.exception;


public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Integer id) {
        super("Product not found with id " + id);
    }
    public ProductNotFoundException(String message) {
        super(message);
    }
}
