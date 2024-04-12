package com.mattcom.demostoreapp.requestmodels;


import com.mattcom.demostoreapp.user.address.Address;
import com.mattcom.demostoreapp.user.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Setter
@Getter
public class StoreUserRequest {

    private int id;

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

    private String phoneNumber;
    private Set<Role> roles;
    private Set<Address> addresses;


    public StoreUserRequest() {
    }

}
