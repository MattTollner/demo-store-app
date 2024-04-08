package com.mattcom.demostoreapp.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.mattcom.demostoreapp.auth.reqres.LoginInfo;
import com.mattcom.demostoreapp.auth.reqres.PasswordResetInfo;
import com.mattcom.demostoreapp.auth.EncryptionService;
import com.mattcom.demostoreapp.auth.JWTService;
import com.mattcom.demostoreapp.user.role.RoleRepository;
import com.mattcom.demostoreapp.user.StoreUserRepository;
import com.mattcom.demostoreapp.auth.token.VerificationTokenRepository;
import com.mattcom.demostoreapp.user.role.Role;
import com.mattcom.demostoreapp.user.StoreUser;
import com.mattcom.demostoreapp.auth.token.VerificationToken;
import com.mattcom.demostoreapp.email.exception.FailureToSendEmailException;
import com.mattcom.demostoreapp.user.exception.StoreUserExistsException;
import com.mattcom.demostoreapp.user.exception.UserNotVerifiedException;
import com.mattcom.demostoreapp.requestmodels.StoreUserRequest;
import com.mattcom.demostoreapp.user.StoreUserService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreUserServiceTest {

    @RegisterExtension
    private static GreenMailExtension greenMailConfig = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("springboot", "secret"))
            .withPerMethodLifecycle(true); //Wipes all emails after each method

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private StoreUserRepository storeUserRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JWTService jwtService;

    @Autowired
    MockMvc mockMvc;


//    @Test
//    @Transactional
//    public void testCreateUserWithRoles() {
//        StoreUserRequest storeUserRequest = new StoreUserRequest();
//        storeUserRequest.setEmail("testCreateUserWithRoles@live.co.uk");
//        storeUserRequest.setFirstName("testCreateUserWithRolesFirstName");
//        storeUserRequest.setLastName("testCreateUserWithRolesLastName");
//        storeUserRequest.setPassword("testCreateUserWithRolesPassword");
//        Role adminRole = roleRepository.findByRoleName("ADMIN").get();
//        storeUserRequest.setRoles(Set.of(adminRole));
//        Assertions.assertDoesNotThrow(() -> {
//            storeUserService.registerUser(storeUserRequest);
//        }, "User should register successfully");
//    }

    @Test
    @Transactional
    public void testUpdateUser() throws Exception {
        //new StoreUserHelperMethodsTest().testUserValidation(mockMvc, "/api/users/update", false);
        List<StoreUser> users = storeUserRepository.findAll();
        StoreUserRequest user = new StoreUserRequest();
        user.setId(1);
        user.setFirstName("newName");
        user.setLastName("newLastName");
        user.setPhoneNumber("123456789");

        Assertions.assertDoesNotThrow(() -> {
            StoreUser saved = storeUserService.updateUser(user);
            System.out.println("");
        }, "User should update successfully");
    }

    @Test
    @Transactional //Because its transactional the modifications to the database will be rolled back
    public void testRegisterUser() throws MessagingException {
        StoreUserRequest registrationInfo = new StoreUserRequest();
        registrationInfo.setEmail("user1@example.com");
        registrationInfo.setFirstName("firstName");
        registrationInfo.setLastName("lastName");
        registrationInfo.setPassword("password");
        Assertions.assertThrows(StoreUserExistsException.class, () -> {
            storeUserService.registerUser(registrationInfo);
        }, "User with email user1@example.com already exists");

        registrationInfo.setEmail("testRegisterUser1@example.com");
        Assertions.assertDoesNotThrow(() -> {
            storeUserService.registerUser(registrationInfo);
        }, "User should register successfully");

        Assertions.assertEquals("testRegisterUser1@example.com",
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
        List<StoreUser> users = storeUserService.getUsers();
        Assertions.assertNotNull(storeUserService.loginUser(loginInfo), "login should be successful");

        loginInfo.setEmail("user2@example.com");

        try {
            storeUserService.loginUser(loginInfo);
            Assertions.fail("User should not have email verified");
        } catch (UserNotVerifiedException e) {
            Assertions.assertTrue(e.isNewEmailSent(), "Verification email should be sent");
            Assertions.assertEquals(1, greenMailConfig.getReceivedMessages().length);
        }

        try {
            storeUserService.loginUser(loginInfo);
            Assertions.fail("User should not have email  verified");
        } catch (UserNotVerifiedException e) {
            Assertions.assertFalse(e.isNewEmailSent(), "Verification email should not be resent");
            Assertions.assertEquals(1, greenMailConfig.getReceivedMessages().length);
        }
    }

    @Test
    @Transactional
    public void testVerifyUser() throws FailureToSendEmailException {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setEmail("user2@example.com");
        loginInfo.setPassword("password");
        try {
            storeUserService.loginUser(loginInfo);
            Assertions.fail("User should not have email verified");
        } catch (UserNotVerifiedException e) {
            List<VerificationToken> verificationTokens = verificationTokenRepository.findByStoreUser_IdOrderByIdDesc(2);
            VerificationToken token = verificationTokens.getFirst();
            Assertions.assertTrue(storeUserService.verifyUser(token.getToken()), "User should be verified");
        }
        Assertions.assertFalse(storeUserService.verifyUser("token-incorrect"), "Token should not be valid");
    }

    @Test
    @Transactional
    public void testForgotPassword() throws FailureToSendEmailException, MessagingException {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> storeUserService.forgotPassword("user3@example.com"));
        Assertions.assertDoesNotThrow(() -> storeUserService.forgotPassword("user1@example.com"));
        storeUserService.forgotPassword("user1@example.com");
        Assertions.assertEquals("user1@example.com", greenMailConfig.getReceivedMessages()[0].getRecipients(Message.RecipientType.TO)[0].toString());
    }

    @Test
    public void testResetPassword() throws Exception {
        StoreUser user = storeUserRepository.findByEmailIgnoreCase("user1@example.com").get();
        String token = jwtService.generateResetJWT(user);
        storeUserService.resetPassword(new PasswordResetInfo(token, "password-new"));
        String encryptedPassword = encryptionService.encryptPassword("password-new");
        Assertions.assertEquals(encryptedPassword, storeUserRepository.findByEmailIgnoreCase("user1@example.com").get().getPassword(), "Password should be password-new");
    }

    //Todo test validation on the password field when resetting

}
