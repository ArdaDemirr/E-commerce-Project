package com.advanced.projectspring.dto;

public class UserResponseDTO {
    private Long id;
    private String email;
    private String role;
    private String name;
    private String surname;
    private String gender;
    private boolean active;

    // GETTERS
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    public String getGender() {
        return gender;
    }

    public boolean isActive() {
        return active;
    }

    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
