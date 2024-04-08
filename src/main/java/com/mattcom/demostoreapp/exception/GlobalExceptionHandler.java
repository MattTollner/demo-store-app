package com.mattcom.demostoreapp.exception;

import com.mattcom.demostoreapp.user.exception.UserNotFoundException;
import com.mattcom.demostoreapp.user.exception.UserNotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, new Date());
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<ErrorResponse> handleUserNotLoggedInException(UserNotLoggedInException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, new Date());
        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }
}
