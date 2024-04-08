package com.mattcom.demostoreapp.user.exception;

public class UserNotLoggedInException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
