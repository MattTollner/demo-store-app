package com.mattcom.demostoreapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mattcom.demostoreapp.entity.StoreUser;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expireInSeconds}")
    private int expireInSeconds;

    private Algorithm algorithm;

    private static final String EMAIL_KEY = "EMAIL";
    private static final String EMAIL_AUTH_KEY = "EMAIL_AUTH";

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String gererateJWT(StoreUser user) {
        return JWT.create()
                .withClaim(EMAIL_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expireInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String generateVerificationJWT(StoreUser user) {
        return JWT.create()
                .withClaim(EMAIL_AUTH_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expireInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }


    public String getEmail(String token) {
        return JWT.decode(token).getClaim(EMAIL_KEY).asString();
    }
}

