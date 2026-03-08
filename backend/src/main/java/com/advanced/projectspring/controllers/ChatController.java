package com.advanced.projectspring.controllers;

import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.chat.ChatRequest;
import com.advanced.projectspring.dto.chat.ChatResponse;
import com.advanced.projectspring.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// exposes the endpoint for chat-AI

@RestController
@RequestMapping("/api/chat") // endpoint
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    @Autowired
    private ChatService chatService; // inejct service

    @Autowired
    private JwtUtil jwtUtil; // inject Util - to validate/open token

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(
            @RequestBody ChatRequest request, // sends a JSON object containing the user's message
            @RequestHeader("Authorization") String authHeader) { // incoming token to inspect

        // Extract token
        String token = authHeader.substring(7);

        // Extract user info from token — no database call needed
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);
        Long userId = jwtUtil.extractUserId(token);

        ChatResponse response = chatService.processMessage(request, email, role, userId);
        return ResponseEntity.ok(response);
    }
}
