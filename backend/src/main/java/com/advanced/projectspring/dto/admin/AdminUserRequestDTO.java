package com.advanced.projectspring.dto.admin;

public class AdminUserRequestDTO {
    private String role;
    private boolean active;

    // GETTERS
    public String getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    // SETTERS
    public void setRole(String role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}