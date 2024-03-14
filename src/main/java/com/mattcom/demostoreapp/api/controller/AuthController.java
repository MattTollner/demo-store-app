package com.mattcom.demostoreapp.api.controller;

import com.mattcom.demostoreapp.api.model.LoginInfo;
import com.mattcom.demostoreapp.api.model.LoginResponse;
import com.mattcom.demostoreapp.api.model.RegistrationInfo;
import com.mattcom.demostoreapp.exception.FailureToSendEmailException;
import com.mattcom.demostoreapp.exception.StoreUserExistsException;
import com.mattcom.demostoreapp.exception.UserNotVerifiedException;
import com.mattcom.demostoreapp.service.StoreUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private StoreUserService storeUserSerivice;

    public AuthController(StoreUserService storeUserSerivice) {
        this.storeUserSerivice = storeUserSerivice;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationInfo registrationInfo) {
        try {
            storeUserSerivice.registerUser(registrationInfo);
            return ResponseEntity.ok().build();
        } catch (StoreUserExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (FailureToSendEmailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity verifyUser(@RequestParam String token) {
        if(storeUserSerivice.verifyUser(token)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginInfo loginInfo) {
        String jwt = null;
        try {
            jwt = storeUserSerivice.loginUser(loginInfo);
        } catch (FailureToSendEmailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserNotVerifiedException e) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setSuccess(false);
            String response = "USER_NOT_VERIFIED";
            if (e.isNewEmailSent()) {
                response = "USER_NOT_VERIFIED_EMAIL_SENT";
            }
            loginResponse.setFailureMessage(response);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(loginResponse);
        }
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setJwt(jwt);
            loginResponse.setSuccess(true);
            return ResponseEntity.ok(loginResponse);
        }
    }
}
