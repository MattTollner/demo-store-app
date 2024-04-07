package com.mattcom.demostoreapp.api;


import com.stripe.exception.ApiException;
import org.springframework.http.ResponseEntity;

public class ApiExceptionHandler {


    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        new API
        ApiException apiException = new ApiException(e.getMessage(), e.getHttpStatus());
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
}
