package com.mattcom.demostoreapp.service;

import com.mattcom.demostoreapp.entity.VerificationToken;
import com.mattcom.demostoreapp.exception.FailureToSendEmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${email.from}")
    private String from;

    @Value("${app.frontend.url}")
    private String appFrontendUrl;

    private JavaMailSender javaMailSender;


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private SimpleMailMessage createMailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        return message;
    }

    public void sendVerificationToken(VerificationToken token) throws FailureToSendEmailException {
        SimpleMailMessage message = createMailMessage();
        message.setTo(token.getStoreUser().getEmail());
        String subject = "Please confirm your email";
        String text = "To confirm your email, please click here: " + appFrontendUrl + "/auth/verify" + "?token=" + token.getToken();
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new FailureToSendEmailException();
        }
    }


}
