package com.mattcom.demostoreapp.service;


import com.mattcom.demostoreapp.auth.EncryptionService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
// Need this as it will re run spring which re runs the sql causing errors, therefor for now all test classes need this so they all run under the same build
public class EncryptionServiceTest {

    @Autowired
    private EncryptionService encryptionService;

    @Test
    public void testEncryptPassword() {
        String password = "password";
        String hash = encryptionService.encryptPassword(password);
        Assertions.assertTrue(encryptionService.verifyPassword(password, hash), "Password should match hash");
        Assertions.assertFalse(encryptionService.verifyPassword("wrongPassword", hash), "Password should not match hash");
    }


}
