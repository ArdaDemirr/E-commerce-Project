/*
 * LoginRequest.java
 *
 * 1. Login request body
 *
 * DTO stands for Data Transfer Object. They are simple classes that define exactly what data 
 * goes in and out of your endpoints.
 */

package com.advanced.projectspring.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest() {
    }

    // Getters — AuthService reads these
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters — Spring fills these when converting from JSON
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
