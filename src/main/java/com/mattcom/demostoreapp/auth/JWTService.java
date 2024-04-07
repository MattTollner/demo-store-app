package com.mattcom.demostoreapp.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mattcom.demostoreapp.user.StoreUser;
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
    private static final String ID_KEY = "USER_ID";
    private static final String ROLE_KEY = "USER_ROLES";
    private static final String EMAIL_AUTH_KEY = "EMAIL_AUTH";
    private static final String EMAIL_RESET_KEY = "EMAIL_RESET";

    @PostConstruct
    public void postConstruct() {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    //Token used when user logs in
    public String gererateJWT(StoreUser user) {
        return JWT.create()
                .withClaim(EMAIL_KEY, user.getEmail())
                .withClaim(ID_KEY, String.valueOf(user.getId()))
                .withArrayClaim(ROLE_KEY, user.getRolesAsStringArray())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * expireInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }


    //Token used to verify user, only sent in conformation email
    public String generateVerificationJWT(StoreUser user) {
        return JWT.create()
                .withClaim(EMAIL_AUTH_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * expireInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    //Token used to reset password, only sent in conformation email
    //Security issue todo, we do not store this token in the database which means it could be used again after initial use.
    public String generateResetJWT(StoreUser user) {
        return JWT.create()
                .withClaim(EMAIL_RESET_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * 60 * 15)))
                .withIssuer(issuer)
                .sign(algorithm);
    }


    public String getEmail(String token) {
        DecodedJWT jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token); //This line ensures that the algorithm is used when decoding to ensure that only tokens encrypted with our algorithm pass
        return jwt.getClaim(EMAIL_KEY).asString();
    }

    public String getUserId(String token) {
        DecodedJWT jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token); //This line ensures that the algorithm is used when decoding to ensure that only tokens encrypted with our algorithm pass
        return jwt.getClaim(ID_KEY).asString();
    }

    public String getEmailResetPassword(String token) {
        DecodedJWT jwt = JWT.require(algorithm).withIssuer(issuer).build().verify(token); //This line ensures that the algorithm is used when decoding to ensure that only tokens encrypted with our algorithm pass
        return jwt.getClaim(EMAIL_RESET_KEY).asString();
    }
}

