package com.mattcom.demostoreapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.mattcom.demostoreapp.dao.StoreUserRepository;
import com.mattcom.demostoreapp.entity.StoreUser;
import com.mattcom.demostoreapp.entity.VerificationToken;
import com.mattcom.demostoreapp.service.JWTService;
import com.mattcom.demostoreapp.service.StoreUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class JWTServiceTest {


    @Autowired
    private JWTService jwtService;

    @Autowired
    private StoreUserRepository storeUserRepository;

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;


    @Test
    public void testVerifyUserToken() {
        Optional<StoreUser> storeUser = storeUserRepository.findByEmailIgnoreCase("user1@example.com");
        String token = jwtService.generateVerificationJWT(storeUser.get());
        Assertions.assertNull(jwtService.getEmail(token), "Token should not have email");
    }

    @Test
    public void testLoginUserToken() {
        Optional<StoreUser> storeUser = storeUserRepository.findByEmailIgnoreCase("user1@example.com");
        String token = jwtService.gererateJWT(storeUser.get());
        Assertions.assertEquals(storeUser.get().getEmail(), jwtService.getEmail(token), "Token should have email");
    }

    @Test
    public void testInvalidGeneratedLoginJWT() {
        String token = JWT.create().withClaim("EMAIL", "user1@example.com").sign(Algorithm.HMAC256("InvalidKey"));
        Assertions.assertThrows(SignatureVerificationException.class, () -> jwtService.getEmail(token));
    }

    @Test
    public void testValidTokenNoIssuerLogin() {
        String token = JWT.create().withClaim("EMAIL", "user1@example.com").sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class, () -> jwtService.getEmail(token));
    }

    @Test
    public void testInvalidGeneratedPassResetJWT() {
        String token = JWT.create().withClaim("EMAIL_RESET", "user1@example.com").sign(Algorithm.HMAC256("InvalidKey"));
        Assertions.assertThrows(SignatureVerificationException.class, () -> jwtService.getEmailResetPassword(token));
    }

    @Test
    public void testValidTokenNoIssuerPassReset() {
        String token = JWT.create().withClaim("EMAIL_RESET", "user1@example.com").sign(Algorithm.HMAC256(algorithmKey));
        Assertions.assertThrows(MissingClaimException.class, () -> jwtService.getEmailResetPassword(token));
    }


    @Test
    public void testPasswordResetToken() {
        Optional<StoreUser> storeUser = storeUserRepository.findByEmailIgnoreCase("user1@example.com");
        String token = jwtService.generateResetJWT(storeUser.get());
        Assertions.assertEquals(storeUser.get().getEmail(), jwtService.getEmailResetPassword(token), "Token should have email");
    }
}
