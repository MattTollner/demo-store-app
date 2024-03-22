package com.mattcom.demostoreapp.api.controller;

import com.mattcom.demostoreapp.api.model.*;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.exception.FailureToSendEmailException;
import com.mattcom.demostoreapp.exception.StoreUserExistsException;
import com.mattcom.demostoreapp.exception.UserNotVerifiedException;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import com.mattcom.demostoreapp.service.JWTService;
import com.mattcom.demostoreapp.service.StoreUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private StoreUserService storeUserSerivice;
    private JWTService jwtService;

    public AuthController(StoreUserService storeUserSerivice, JWTService jwtService) {
        this.storeUserSerivice = storeUserSerivice;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody StoreUserRequest registrationInfo) {
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
        if (storeUserSerivice.verifyUser(token)) {
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
            loginResponse.setUserId(jwtService.getUserId(jwt));
            ResponseCookie cookie = ResponseCookie.from("accessToken", jwt)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofSeconds(60))
                    .build();
            return ResponseEntity.ok().body(loginResponse);
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity forgotPassword(@RequestParam String email) {
        try {
            storeUserSerivice.forgotPassword(email);
        } catch (FailureToSendEmailException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody PasswordResetInfo passwordResetInfo) throws Exception {
        storeUserSerivice.resetPassword(passwordResetInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public StoreUser getLoggedInUserProfile(@AuthenticationPrincipal StoreUser user) {
        return user;
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<IsAuthInfo> isAuthenticated(@AuthenticationPrincipal StoreUser user) {
        if (user == null) {
            IsAuthInfo isAuthInfo = new IsAuthInfo();
            isAuthInfo.setAuthenticated(false);
            isAuthInfo.setErrorMessage("USER_NOT_LOGGED_IN");
            return ResponseEntity.ok(isAuthInfo);
        }

        IsAuthInfo isAuthInfo = new IsAuthInfo();
        isAuthInfo.setAuthenticated(true);
        isAuthInfo.setUserId(String.valueOf(user.getId()));
        return ResponseEntity.ok(isAuthInfo);
    }

}



