package com.advanced.projectspring.auth;

import com.advanced.projectspring.auth.dto.LoginRequest;
import com.advanced.projectspring.auth.dto.LoginResponse;
import com.advanced.projectspring.auth.dto.RegisterRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ─── Helper: Set both tokens as HttpOnly cookies
    // ──────────────────────────────
    private void setAuthCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        // Access token: HttpOnly, valid for 15 minutes, available to all paths
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(900) // 15 minutes in seconds
                .sameSite("Strict")
                .build();

        // Refresh token: HttpOnly, valid for 7 days, ONLY sent to /api/auth/refresh
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/api/auth/refresh")
                .maxAge(604800) // 7 days in seconds
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    // ─── Register ────────────────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        Map<String, Object> result = authService.register(request);

        @SuppressWarnings("unchecked")
        Map<String, String> tokens = (Map<String, String>) result.get("tokens");
        LoginResponse body = (LoginResponse) result.get("body");

        setAuthCookies(response, tokens.get("accessToken"), tokens.get("refreshToken"));
        return ResponseEntity.ok(body);
    }

    // ─── Login ───────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Map<String, Object> result = authService.login(request);

        @SuppressWarnings("unchecked")
        Map<String, String> tokens = (Map<String, String>) result.get("tokens");
        LoginResponse body = (LoginResponse) result.get("body");

        setAuthCookies(response, tokens.get("accessToken"), tokens.get("refreshToken"));
        return ResponseEntity.ok(body);
    }

    // ─── Refresh ─────────────────────────────────────────────────────────────────
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        // Find the refresh_token cookie
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(401).body("No refresh token found");
        }

        // Validate and generate a new access token
        String newAccessToken = authService.refreshAccessToken(refreshToken);

        // Set the new access_token cookie
        ResponseCookie accessCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .path("/")
                .maxAge(900)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseEntity.ok().body("Token refreshed successfully");
    }

    // ─── Logout ──────────────────────────────────────────────────────────────────
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Delete both cookies by setting Max-Age to 0
        ResponseCookie clearAccess = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        ResponseCookie clearRefresh = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .path("/api/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

        return ResponseEntity.ok().body("Logged out successfully");
    }
}
