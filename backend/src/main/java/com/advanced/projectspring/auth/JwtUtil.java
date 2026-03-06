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
    // which reads it from .env
    // so the actual secret key never appears in the code

    @Value("${jwt.expiration}")
    private Long expiration;
    // Reads jwt.expiration = 86400000 (milliseconds)
    // 86400000ms = 24 hours
    // token expires after 24 hours, user must login again

    private Key getSigningKey() {
        // Converts our secret string into a proper cryptographic Key object
        // jjwt requires a Key object, not a plain String
        return Keys.hmacShaKeyFor(secret.getBytes());
        // hmacShaKeyFor = creates an HMAC-SHA key
        // .getBytes() = converts String to byte array
    }

    // --------------------Generate JWT token---------------------

    public String generateToken(String email, String role) {
        // Called after successful login or register
        // Returns the JWT string like "eyJhbGci..."

        Map<String, Object> claims = new HashMap<>();
        // claims = the data we want to store INSIDE the token
        // think of it as a small JSON object

        claims.put("role", role);
        // puts the user role into the claims map

        return Jwts.builder()
                // start building the token

                .setClaims(claims)
                // put our claims (role) inside

                .setSubject(email)
                // subject = who this token belongs to
                // we use email as the unique identifier

                .setIssuedAt(new Date())
                // record when the token was created

                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // set when the token expires
                // System.currentTimeMillis() = right now in milliseconds
                // + expiration = + 86400000ms = + 24 hours

                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                // sign the token with our secret key using HS256 algorithm
                // this creates the signature part (3rd part of the token)
                // if anyone changes the payload, signature won't match

                .compact();
        // build and return the final JWT string
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
}
