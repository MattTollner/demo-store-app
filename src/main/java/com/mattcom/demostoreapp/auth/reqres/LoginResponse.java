package com.mattcom.demostoreapp.auth.reqres;


import lombok.Getter;
import lombok.Setter;

//Created this file so if we need extra data in the response at a later date it will be easy to add without changing too much code
@Setter
@Getter
public class LoginResponse {

    private String jwt;
    private boolean success;
    private String failureMessage;
    private String userId;


}
