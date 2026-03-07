/*
 * register() → validate → hash password → save user → return token
 * login()    → find user → verify password → return token
 */

package com.advanced.projectspring.auth;

import com.advanced.projectspring.auth.dto.LoginRequest;
import com.advanced.projectspring.auth.dto.LoginResponse;
import com.advanced.projectspring.auth.dto.RegisterRequest;
// 3 DTOs   

import com.advanced.projectspring.models.User;
// User model — create and save User objects here

import com.advanced.projectspring.repositories.UserRepository;
// to find users by email and save new users

import org.springframework.beans.factory.annotation.Autowired;
// to inject JwtUtil and UserRepository automatically

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// BCrypt = the hashing algorithm for passwords
// industry standard, very secure
// "admin123" → "$2a$10$N9qo8uLOickgx2ZMRZoMye..."

import org.springframework.stereotype.Service;
// @Service = tells Spring this is a service layer class
// similar to @Component but semantically means business logic

import java.util.Optional;
// Optional = safer way to handle nullable values
// instead of returning null when user not found
// we return Optional.empty()

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // private final BCryptPasswordEncoder passwordEncoder = new
    // BCryptPasswordEncoder();
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    // create one BCrypt encoder instance
    // use this to hash passwords and verify them
    // BCrypt automatically adds salt — same password hashes differently each time
    // but checkpw() still works correctly

    public LoginResponse register(RegisterRequest request) {

        // Check if email already exists
        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole().toUpperCase());
        user.setGender(request.getGender());
        user.setActive(true);
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPasswordHash(hashedPassword);

        User savedUser = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole());

        // Return response
        return new LoginResponse(token, savedUser.getRole(), savedUser.getName(), savedUser.getSurname(),
                savedUser.getId());
    }

    public LoginResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        // Check if user is active
        if (!user.isActive()) {
            throw new RuntimeException("Account suspended");
        }

        // Generate token and return
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new LoginResponse(token, user.getRole(), user.getName(), user.getSurname(), user.getId());
    }
}
