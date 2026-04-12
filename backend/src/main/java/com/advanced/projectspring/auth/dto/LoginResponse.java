package com.advanced.projectspring.auth.dto;

// What Angular receives after successful login/register.
// Tokens are NO LONGER in the body — they are set as HttpOnly cookies by the controller.
// Angular only needs metadata to display the UI correctly.
public class LoginResponse {

    private String role;
    // "ADMIN", "CORPORATE", or "INDIVIDUAL"
    // Angular uses this to navigate to the right dashboard

    private String name;
    // The user's display name — shown in the header

    private String surname;

    private Long userId;
    // Needed for profile-related requests

    // Constructor
    public LoginResponse(String role, String name, String surname, Long userId) {
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.userId = userId;
    }

    // Getters
    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Long getUserId() {
        return userId;
    }
}
