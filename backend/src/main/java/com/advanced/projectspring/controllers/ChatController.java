package com.advanced.projectspring.controllers;

import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.chat.ChatRequest;
import com.advanced.projectspring.dto.chat.ChatResponse;
import com.advanced.projectspring.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(
            @RequestBody ChatRequest request,
            @RequestHeader("Authorization") String authHeader) {

        // Extract token
        String token = authHeader.substring(7);

        // Extract user info from token — no database call needed
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);
        Long userId = jwtUtil.extractUserId(token);
        // we use these to build role-based context

        ChatResponse response = chatService.processMessage(request, email, role, userId);
        return ResponseEntity.ok(response);
    }
}
