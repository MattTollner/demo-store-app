//package com.mattcom.demostoreapp.email;
//
//import com.mattcom.demostoreapp.auth.token.VerificationToken;
//import com.mattcom.demostoreapp.email.exception.FailureToSendEmailException;
//import com.mattcom.demostoreapp.user.StoreUser;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class EmailServiceTest {
//
//    @Mock
//    private JavaMailSender javaMailSender;
//
//    @InjectMocks
//    private EmailService emailService;
//
//    @Test
//    public void testSendVerificationToken_Successful() throws FailureToSendEmailException {
//        VerificationToken token = new VerificationToken("token123", new StoreUser("user@example.com"));
//        when(javaMailSender.createMailMessage()).thenReturn(new SimpleMailMessage());
//
//        emailService.sendVerificationToken(token);
//
//        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
//    }
//
//    @Test(expected = FailureToSendEmailException.class)
//    public void testSendVerificationToken_FailureToSendEmail() throws FailureToSendEmailException {
//        VerificationToken token = new VerificationToken("token123", new StoreUser("user@example.com"));
//        when(javaMailSender.createMailMessage()).thenReturn(new SimpleMailMessage());
//        doThrow(new MailException("Failed to send email")).when(javaMailSender).send(any(SimpleMailMessage.class));
//
//        emailService.sendVerificationToken(token);
//    }
//}
