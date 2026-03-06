package com.advanced.projectspring.auth.dto;

// this is what Angular RECEIVES after successful login
// Angular reads the token and stores it in localStorage
// Angular reads the role and navigates to the right dashboard
public class LoginResponse {
    private String token;
    // the JWT token string "eyJhbGci..."
    // Angular stores this and sends it with every future request
    // in the Authorization header

    private String role;
    // "admin", "corporate", or "individual"
    // Angular uses this to decide which page to navigate to:
    // admin → /admin/dashboard
    // corporate → /corporate/dashboard
    // individual → /shop

    private String name;
    // the user's display name
    // Angular shows this in the header "Welcome, John"

    private String surname;

    private Long userId;
    // the user's database ID
    // Angular might need this for future requests
    // like GET /api/users/5/profile

    // Constructor — AuthService uses this to build the response
    public LoginResponse(String token, String role, String name, String surname, Long userId) {
        this.token = token;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.userId = userId;
    }

    // Getters — Spring reads these to convert to JSON
    public String getToken() {
        return token;
    }

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
