package com.mattcom.demostoreapp.auth.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PasswordResetInfo {

    @NotNull
    @NotBlank
    private String token;

    @NotNull
    @NotBlank
    private String password;


    public PasswordResetInfo(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public PasswordResetInfo() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
