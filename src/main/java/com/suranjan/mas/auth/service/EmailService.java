package com.suranjan.mas.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(
            String toEmail,
            String verificationToken
    ) {

        String subject = "Verify Your MAS Account";

        String verificationLink =
                "http://localhost:8080/auth/verify?token="
                        + verificationToken;

        String body =
                "Welcome to MAS!\n\n" +
                        "Please verify your email by clicking the link below:\n\n" +
                        verificationLink +
                        "\n\nThank you.";

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
