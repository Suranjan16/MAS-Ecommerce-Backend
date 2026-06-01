package com.suranjan.mas.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    public ForgotPasswordRequest() {
    }

    public @NotBlank(message = "Email is required") @Email(message = "Enter a valid email address") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Enter a valid email address") String email) {
        this.email = email;
    }
}
