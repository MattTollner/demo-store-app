package com.mattcom.demostoreapp.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.mattcom.demostoreapp.api.model.LoginInfo;
import com.mattcom.demostoreapp.api.model.RegistrationInfo;
import com.mattcom.demostoreapp.exception.FailureToSendEmailException;
import com.mattcom.demostoreapp.exception.StoreUserExistsException;
import com.mattcom.demostoreapp.exception.UserNotVerifiedException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StoreUserServiceTest {

    @RegisterExtension
    private static GreenMailExtension greenMailConfig = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true); //Wipes all emails after each method

    @Autowired
    private StoreUserService storeUserService;

    @Test
    @Transactional //Because its transactional the modifications to the database will be rolled back
    public void testRegisterUser() throws MessagingException {
        RegistrationInfo registrationInfo = new RegistrationInfo();
        registrationInfo.setEmail("user1@example.com");
        registrationInfo.setFirstName("firstName");
        registrationInfo.setLastName("lastName");
        registrationInfo.setPassword("password");
        Assertions.assertThrows(StoreUserExistsException.class, () -> {
            storeUserService.registerUser(registrationInfo);
        }, "User with email user1@example.com already exists");

        registrationInfo.setEmail("testRegisterUser@example.com");
        Assertions.assertDoesNotThrow(() -> {
            storeUserService.registerUser(registrationInfo);
        }, "User should register successfully");

        Assertions.assertEquals("testRegisterUser@example.com",
                greenMailConfig.getReceivedMessages()[0]
                        .getRecipients(Message.RecipientType.TO)[0].toString());

    }

    @Test
    @Transactional
    public void testLoginUser() throws UserNotVerifiedException, FailureToSendEmailException {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("user1@example.com-Notexist");
        loginInfo.setPassword("password-incorrect");
        Assertions.assertNull(storeUserService.loginUser(loginInfo), "the user should not exist");

        loginInfo.setEmail("user1@example.com");
        loginInfo.setPassword("password-imcprrect");
        Assertions.assertNull(storeUserService.loginUser(loginInfo), "password should be incorrect");

        loginInfo.setPassword("password");
        Assertions.assertNotNull(storeUserService.loginUser(loginInfo), "login should be successful");

        loginInfo.setEmail("user2@example.com");

        try{
            storeUserService.loginUser(loginInfo);
            Assertions.assertTrue(false, "User should not have email  verified");
        } catch (UserNotVerifiedException e) {
            Assertions.assertTrue(e.isNewEmailSent(), "Verification email should be sent");
            Assertions.assertEquals(1, greenMailConfig.getReceivedMessages().length);
        }

        try{
            storeUserService.loginUser(loginInfo);
            Assertions.assertTrue(false, "User should not have email  verified");
        } catch (UserNotVerifiedException e) {
            Assertions.assertFalse(e.isNewEmailSent(), "Verification email should not be resent");
            Assertions.assertEquals(1, greenMailConfig.getReceivedMessages().length);
        }
    }

}
