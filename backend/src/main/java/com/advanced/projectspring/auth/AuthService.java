package com.advanced.projectspring.auth;

import com.advanced.projectspring.auth.dto.LoginRequest;
import com.advanced.projectspring.auth.dto.LoginResponse;
import com.advanced.projectspring.auth.dto.RegisterRequest;

import com.advanced.projectspring.models.User;

import com.advanced.projectspring.repositories.UserRepository;
import com.advanced.projectspring.repositories.StoreRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ─── Internal helper ─────────────────────────────────────────────────────────
    // Returns both access and refresh tokens so the controller can set them as cookies.
    private Map<String, String> buildTokenPair(User user) {
        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getId());
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    // ─── Register ────────────────────────────────────────────────────────────────
    public Map<String, Object> register(RegisterRequest request) {

        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new IllegalStateException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole().toUpperCase());
        user.setGender(request.getGender());
        user.setActive(true);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        if ("CORPORATE".equalsIgnoreCase(request.getRole()) && request.getStoreName() != null
                && !request.getStoreName().trim().isEmpty()) {
            com.advanced.projectspring.models.Store store = new com.advanced.projectspring.models.Store();
            store.setName(request.getStoreName().trim());
            store.setStatus("open");
            store.setOwner(savedUser);
            storeRepository.save(store);
        }

        Map<String, String> tokens = buildTokenPair(savedUser);
        LoginResponse body = new LoginResponse(
                savedUser.getRole(), savedUser.getName(), savedUser.getSurname(), savedUser.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("tokens", tokens);
        result.put("body", body);
        return result;
    }

    // ─── Login ───────────────────────────────────────────────────────────────────
    public Map<String, Object> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid password");
        }

        if (!user.isActive()) {
            throw new org.springframework.security.access.AccessDeniedException("Account suspended");
        }

        Map<String, String> tokens = buildTokenPair(user);
        LoginResponse body = new LoginResponse(
                user.getRole(), user.getName(), user.getSurname(), user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("tokens", tokens);
        result.put("body", body);
        return result;
    }

    // ─── Refresh ─────────────────────────────────────────────────────────────────
    // Called by AuthController when the frontend hits POST /api/auth/refresh.
    // Validates the refresh token, fetches the latest user data, and returns a fresh access token.
    public String refreshAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        Long userId = jwtUtil.extractUserId(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new org.springframework.security.access.AccessDeniedException("Account suspended");
        }

        // Generate a fresh access token — role always re-read from DB (handles role changes)
        return jwtUtil.generateToken(user.getEmail(), user.getRole(), userId);
    }
}

