/*
    POST /api/auth/register → calls authService.register()
    POST /api/auth/login    → calls authService.login()
    These are the only two public endpoints in the entire app
 */

package com.advanced.projectspring.auth;

import com.advanced.projectspring.auth.dto.LoginRequest;
import com.advanced.projectspring.auth.dto.LoginResponse;
import com.advanced.projectspring.auth.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // listen for HTTP requests from the frontend and send back JSON data.
@RequestMapping("/api/auth") // path prefix for all endpoints in this controller
@CrossOrigin(origins = "http://localhost:4200") // for security reasons, allows requests from Angular dev server
// LATER add a proper CorsConfig for production
public class AuthController {

    @Autowired // inject AuthService
    private AuthService authService;
    // will use for checking values in database and sending response to frontend

    @PostMapping("/register") // register endpoint
    // handles POST /api/auth/register
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // @RequestBody → Spring reads JSON from request body
        // and converts it to RegisterRequest object automatically
        // { "name": "John", "email": "john@email.com", ... }
        // → RegisterRequest with fields filled

        LoginResponse response = authService.register(request);
        // call AuthService.register()
        // returns LoginResponse with token + role + name + userId

        return ResponseEntity.ok(response);
        // 200 OK + LoginResponse as JSON body
        // Angular receives:
        // { "token": "eyJhbGci...", "role": "individual", "name": "John", "userId": 5 }
    }

    @PostMapping("/login")
    // handles POST /api/auth/login
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // @RequestBody → converts JSON to LoginRequest
        // { "email": "admin@platform.com", "password": "admin123" }
        // → LoginRequest with email and password filled

        LoginResponse response = authService.login(request);
        // call AuthService.login()
        // returns LoginResponse with token + role + name + userId

        return ResponseEntity.ok(response);
        // 200 OK + LoginResponse as JSON
    }
}
