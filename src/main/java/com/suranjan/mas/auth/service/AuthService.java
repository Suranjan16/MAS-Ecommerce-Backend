package com.suranjan.mas.auth.service;

import com.suranjan.mas.auth.dto.AuthResponse;
import com.suranjan.mas.auth.dto.LoginRequest;
import com.suranjan.mas.auth.dto.SignupRequest;
import com.suranjan.mas.auth.dto.UserResponse;
import com.suranjan.mas.auth.entity.Role;
import com.suranjan.mas.auth.entity.User;
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

}
