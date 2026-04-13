package com.advanced.projectspring.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        // ── Step 1: Try to read token from HttpOnly cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // ── Step 2: Fallback to Authorization header
        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        // ── Step 3: If no token found, let Spring Security handle it
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // ── Step 4: Validate token (BUG #1 FIXED)
        if (!jwtUtil.validateToken(token)) {
            // Do NOT return 401 here!
            // We must let the request continue so public endpoints (like /refresh) can
            // still work.
            // Protected endpoints will be automatically blocked by Spring Security.
            filterChain.doFilter(request, response);
            return;
        }

        // ── Step 5: Extract claims
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);
        Long userId = jwtUtil.extractUserId(token);

        if (userId != null)
            request.setAttribute("userId", userId);
        if (email != null)
            request.setAttribute("email", email);
        if (role != null)
            request.setAttribute("role", role);

        // BUG #2 FIXED: Prevent NullPointerException if role is missing
        List<SimpleGrantedAuthority> authorities = (role != null)
                ? List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                : List.of();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email,
                null,
                authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}