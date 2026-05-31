package com.suranjan.mas.auth.controller;

import com.suranjan.mas.auth.dto.AuthResponse;
import com.suranjan.mas.auth.dto.LoginRequest;
import com.suranjan.mas.auth.dto.SignupRequest;
import com.suranjan.mas.auth.dto.UserResponse;
import com.suranjan.mas.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public UserResponse signup(
            @Valid @RequestBody SignupRequest signupRequest
    ) {
        return service.signup(signupRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return service.login(request);
    }

    @GetMapping("/verify")
    public String verifyEmail(
            @RequestParam String token
    ) {
        return service.verifyEmail(token);
    }

}
