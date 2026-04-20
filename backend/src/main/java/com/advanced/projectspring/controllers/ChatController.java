package com.advanced.projectspring.controllers;

//import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.chat.ChatRequest;
import com.advanced.projectspring.dto.chat.ChatResponse;
import com.advanced.projectspring.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

// exposes the endpoint for chat-AI

@RestController
@RequestMapping("/api/chat") // endpoint
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ChatController {

    @Autowired
    private ChatService chatService; // inejct service

    // @Autowired
    // private JwtUtil jwtUtil; // inject Util - to validate/open token

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(
            @Valid @RequestBody ChatRequest requestDTO,
            HttpServletRequest request) {

        // Extract user info from attributes set by JwtAuthFilter
        // These will be null if no token was provided (guest user)
        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");

        // If user is not authenticated, treat them as GUEST
        // GUEST users can only ask general public questions (no personal data)
        if (role == null || userId == null) {
            role = "GUEST";
            userId = 0L;
        }

        ChatResponse response = chatService.processMessage(requestDTO, role, userId);
        return ResponseEntity.ok(response);
    }
}
