package com.mattcom.demostoreapp.auth;

import com.mattcom.demostoreapp.auth.reqres.*;
import com.mattcom.demostoreapp.email.exception.FailureToSendEmailException;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.user.StoreUserService;
import com.mattcom.demostoreapp.user.exception.StoreUserExistsException;
import com.mattcom.demostoreapp.user.exception.UserNotVerifiedException;
import com.mattcom.demostoreapp.user.role.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private StoreUserService storeUserSerivice;
    private JWTService jwtService;
    private RoleRepository roleRepository;

    public AuthController(StoreUserService storeUserSerivice, JWTService jwtService, RoleRepository roleRepository) {
        this.storeUserSerivice = storeUserSerivice;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
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



