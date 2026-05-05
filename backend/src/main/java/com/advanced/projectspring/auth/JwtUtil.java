/*
 * JwtUtil.java
 *
 * 1. Generate JWT token
 * 2. Validate JWT token
 * 3. Extract username from JWT token
 */

package com.advanced.projectspring.auth;

import io.jsonwebtoken.Claims;
// Claims = the data stored INSIDE the token
// like a map: { "sub": "admin@platform.com", "role": "admin", "exp": 1234567 }
import io.jsonwebtoken.Jwts;
// Jwts = the main JWT builder/parser class from the jjwt library

import io.jsonwebtoken.SignatureAlgorithm;
// The algorithm we use to sign the token — we'll use HS256

import io.jsonwebtoken.security.Keys;
// Keys = helper to create a secure signing key from our secret string

import org.springframework.beans.factory.annotation.Value;
// @Value = reads a value from application.properties
// we use it to read jwt.secret and jwt.expiration

import org.springframework.stereotype.Component;
// @Component = tells Spring "create one instance of this class
// and make it available everywhere via @Autowired"

import java.security.Key;
// Key = Java's representation of a cryptographic key

import java.util.Date;
// Date = used for token issue time and expiry time

import java.util.HashMap;
// HashMap = used to build the claims map

import java.util.Map;
// Map = the type for claims

// Any class that needs JwtUtil just adds @Autowired JwtUtil jwtUtil
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    // Reads jwt.secret from application.properties
    // so the actual secret key never appears in the code, gitignored

    @Value("${jwt.expiration}")
    private Long expiration;
    // Reads jwt.expiration = 900000 (milliseconds)
    // 900000ms = 15 minutes
    // token expires after 15 minutes, refresh token extends the session

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;
    // Reads jwt.refresh-expiration = 604800000 (milliseconds)
    // 604800000ms = 7 days
    // refresh token keeps the user logged in for 7 days

    private Key getSigningKey() {
        // Converts our secret string into a proper cryptographic Key object
        // jjwt requires a Key object, not a plain String
        return Keys.hmacShaKeyFor(secret.getBytes());
        // hmacShaKeyFor = creates an HMAC-SHA key
        // .getBytes() = converts String to byte array
    }

    // --------------------Generate JWT token---------------------

    public String generateToken(String email, String role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email, Long userId) {
        // Refresh token only contains the subject (email) and userId
        // It does NOT contain role — roles can change, refresh should re-validate from DB
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", "refresh"); // marker to distinguish from access tokens

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --------------------Validate JWT token---------------------

    public Boolean validateToken(String token) {
        // Called by JwtAuthFilter on every request
        // Returns true if token is valid, false if not

        try {
            Jwts.parserBuilder()

                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            // try to parse and verify the token
            // if signature is wrong → throws exception
            // if token is expired → throws exception
            // if token is malformed → throws exception

            return true;
            // no exception = token is valid
        } catch (Exception e) {
            // any exception = token is invalid
            return false;
        }
    }

    public String extractEmail(String token) {
        // Called by JwtAuthFilter after validateToken() returns true
        // Returns the email stored inside the token
        return extractAllClaims(token).getSubject();
        // getSubject() returns what we set with setSubject(email) above
    }

    public String extractRole(String token) {
        // Called by JwtAuthFilter to get the role
        // Returns "admin", "corporate", or "individual"
        return (String) extractAllClaims(token).get("role");
        // .get("role") reads what we stored with claims.put("role", role) above
    }

    private Claims extractAllClaims(String token) {
        // Private helper method used by extractEmail and extractRole
        // Parses the token and returns all the data inside it
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        // .getBody() returns the Claims object
        // which contains subject, role, issuedAt, expiration
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Number userIdNumber = (Number) claims.get("userId");
        if (userIdNumber != null) {
            return userIdNumber.longValue();
        }
        return null;
    }
}
