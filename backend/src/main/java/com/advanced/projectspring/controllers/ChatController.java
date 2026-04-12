package com.advanced.projectspring.controllers;

import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.chat.ChatRequest;
import com.advanced.projectspring.dto.chat.ChatResponse;
import com.advanced.projectspring.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

// exposes the endpoint for chat-AI

@RestController
@RequestMapping("/api/chat") // endpoint
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ChatController {

    @Autowired
    private ChatService chatService; // inejct service

    @Autowired
    private JwtUtil jwtUtil; // inject Util - to validate/open token

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(
            @RequestBody ChatRequest requestDTO, // sends a JSON object containing the user's message
            HttpServletRequest request) { // Use request to access attributes

        // Extract user info from attributes set by JwtAuthFilter
        String email = (String) request.getAttribute("email");
        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");

        ChatResponse response = chatService.processMessage(requestDTO, email, role, userId);
        return ResponseEntity.ok(response);
    }
}
