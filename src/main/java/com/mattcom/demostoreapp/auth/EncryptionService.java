package com.mattcom.demostoreapp.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    //How many times the password will be salted
    @Value("${encryption.salt.rounds}")
    private int saltRounds;

    private String salt;

    //Spring will first create an instance then does the injection. There for we assign after construct
    @PostConstruct
    public void postConstruct(){
        salt = BCrypt.gensalt(saltRounds);
    }

    public String encryptPassword(String password){
        return BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password, String hash){
        return BCrypt.checkpw(password,hash);
    }





}
