package com.mattcom.demostoreapp.auth;

import com.mattcom.demostoreapp.auth.reqres.*;
import com.mattcom.demostoreapp.email.exception.FailureToSendEmailException;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.StoreUserService;
import com.mattcom.demostoreapp.user.exception.StoreUserExistsException;
import com.mattcom.demostoreapp.user.exception.UserNotVerifiedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final StoreUserService storeUserSerivice;
    private final JWTService jwtService;


    public AuthController(StoreUserService storeUserSerivice, JWTService jwtService) {
        this.storeUserSerivice = storeUserSerivice;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> registerUser(@Valid @RequestBody StoreUserRequest registrationInfo) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            storeUserSerivice.registerUser(registrationInfo);
            loginResponse.setSuccess(true);
            return ResponseEntity.ok().body(loginResponse);
        } catch (StoreUserExistsException e) {
            loginResponse.setSuccess(false);
            loginResponse.setFailureMessage("USER_EXISTS");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(loginResponse);
        } catch (FailureToSendEmailException e) {
            loginResponse.setSuccess(false);
            loginResponse.setFailureMessage("EMAIL_FAILURE");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<LoginResponse> verifyUser(@RequestBody AuthResponse authResponse) {
        LoginResponse loginResponse = new LoginResponse();
        if (storeUserSerivice.verifyUser(authResponse.getToken())) {
            loginResponse.setSuccess(true);
            return ResponseEntity.ok().body(loginResponse);
        }
        loginResponse.setSuccess(false);
        loginResponse.setFailureMessage("USER_NOT_VERIFIED");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginInfo loginInfo) {
        String jwt;
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
            return ResponseEntity.ok().body(loginResponse);
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestParam String email) {
        storeUserSerivice.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<HttpStatus> resetPassword(@Valid @RequestBody PasswordResetInfo passwordResetInfo) {
        storeUserSerivice.resetPassword(passwordResetInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public StoreUser getLoggedInUserProfile(@AuthenticationPrincipal StoreUser user) {
        return user;
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<IsAuthInfo> isAuthenticated(@AuthenticationPrincipal StoreUser user, @RequestBody String role) {
        IsAuthInfo isAuthInfo = new IsAuthInfo();
        isAuthInfo.setAuthenticated(true);
        if (user == null) {
            isAuthInfo.setAuthenticated(false);
            isAuthInfo.setErrorMessage("USER_NOT_LOGGED_IN");
            return ResponseEntity.ok(isAuthInfo);
        }

        isAuthInfo.setUserId(String.valueOf(user.getId()));
        if (!role.isEmpty()) {
            Optional<Boolean> hasRole = user.getRoles().stream().map((role1 -> role1.getRoleName().equals(role))).findFirst();
            if (hasRole.isEmpty()) {
                isAuthInfo.setAuthenticated(false);
                isAuthInfo.setErrorMessage("USER_NOT_AUTHORIZED");
                return ResponseEntity.ok(isAuthInfo);
            }
        }

        return ResponseEntity.ok(isAuthInfo);
    }

}



