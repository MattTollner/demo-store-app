package com.mattcom.demostoreapp.auth.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginInfo {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;


}
