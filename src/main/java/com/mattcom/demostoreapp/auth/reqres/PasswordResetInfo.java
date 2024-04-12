package com.mattcom.demostoreapp.auth.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
