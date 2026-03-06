/*
 * JwtAuthFilter.java
 *
 * 1. Validate JWT token
 * 2. Extract email and role from JWT token
 * 3. Set authenticated user in SecurityContext
 */

package com.advanced.projectspring.auth;

import jakarta.servlet.FilterChain;
// FilterChain = the chain of filters Spring runs on every request
// after our filter finishes, we call chain.doFilter() to pass request forward

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
// HttpServletRequest = the incoming HTTP request object
// we read the Authorization header from this

import jakarta.servlet.http.HttpServletResponse;
// HttpServletResponse = the outgoing HTTP response object
// if token is invalid we write 401 to this

import org.springframework.beans.factory.annotation.Autowired;
// @Autowired = tells Spring to inject JwtUtil automatically

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// This is Spring Security's way of representing an authenticated user
// we create one of these after validating the token

import org.springframework.security.core.authority.SimpleGrantedAuthority;
// SimpleGrantedAuthority = represents a role/permission in Spring Security
// we wrap our "admin"/"corporate"/"individual" string in this

import org.springframework.security.core.context.SecurityContextHolder;
// SecurityContextHolder = Spring Security's storage for the current user
// we store the authenticated user here so controllers can access it

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
// OncePerRequestFilter = base class that guarantees our filter
// runs exactly ONCE per request, not multiple times

import java.io.IOException;
import java.util.List;

@Component
// extends OncePerRequestFilter → Spring calls doFilterInternal() on every
// request
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // This method runs on EVERY incoming request
    // request = what came in
    // response = what we send back
    // filterChain = the next filter in line
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            // Token is invalid (wrong signature, expired, malformed)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
            // return here do not let it pass
        }

        // Extract email and role from inside the token
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email,
                // principal = who is this user (we use email)

                null,
                // credentials = password (null because we already verified via token)

                List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        // authorities = what roles this user has
        // Spring Security expects roles prefixed with "ROLE_"
        // so "admin" becomes "ROLE_ADMIN"
        // "corporate" becomes "ROLE_CORPORATE"
        // "individual" becomes "ROLE_INDIVIDUAL"
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Store the authenticated user in Spring Security's context
        // now any controller can call:
        // SecurityContextHolder.getContext().getAuthentication().getName()
        // to get the current user's email

        filterChain.doFilter(request, response);
        // Pass the request to the next filter / controller
        // without this line, the request would stop here
    }
}
