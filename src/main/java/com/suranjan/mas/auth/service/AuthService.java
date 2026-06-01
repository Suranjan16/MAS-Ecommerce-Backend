package com.suranjan.mas.auth.service;

import com.suranjan.mas.auth.dto.*;
import com.suranjan.mas.auth.entity.*;
import com.suranjan.mas.auth.repository.UserRepository;
import com.suranjan.mas.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public UserResponse signup(SignupRequest signupRequest) {

        if (repository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String verificationToken =
                UUID.randomUUID().toString();

        User user = new User();

        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        signupRequest.getPassword()
                )
        );
        user.setRole(Role.USER);

        user.setVerified(false);
        user.setVerificationToken(
                verificationToken
        );

        User savedUser = repository.save(user);

        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                verificationToken
        );

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = repository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        ));

        if (!user.isVerified()) {
            throw new RuntimeException(
                    "Please verify your email before login"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getRole().name()
        );
    }

    public String verifyEmail(String token) {

        User user =
                repository.findByVerificationToken(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid verification token"
                                ));

        user.setVerified(true);

        user.setVerificationToken(null);

        repository.save(user);

        return "Email verified successfully";
    }

    public String forgotPassword(
            ForgotPasswordRequest request
    ) {

        User user = repository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));

        String resetToken =
                UUID.randomUUID().toString();

        user.setResetPasswordToken(
                resetToken
        );

        repository.save(user);

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                resetToken
        );

        return "Password reset link sent to your email";
    }

    public String resetPassword(ResetPasswordRequest request) {

        User user =
                repository.findByResetPasswordToken(
                        request.getToken()
                ).orElseThrow(() ->
                        new RuntimeException("Invalid reset token"));

        System.out.println("Before update:");
        System.out.println("Verified = " + user.isVerified());
        System.out.println("Password = " + user.getPassword());

        String encodedPassword =
                passwordEncoder.encode(
                        request.getNewPassword()
                );

        System.out.println("Encoded Password = " + encodedPassword);

        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);

        repository.save(user);

        System.out.println("After save:");
        System.out.println("Verified = " + user.isVerified());
        System.out.println("Password = " + user.getPassword());

        return "Password reset successful";
    }

}
