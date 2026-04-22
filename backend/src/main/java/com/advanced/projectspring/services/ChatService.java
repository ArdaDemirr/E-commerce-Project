package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.chat.ChatRequest;
import com.advanced.projectspring.dto.chat.ChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String PYTHON_API_URL = "http://localhost:8000/agent/ask";

    // LAYER 1 SECURITY: Block injection attempts BEFORE calling the Python Agent
    private static final List<String> BLOCKED_PATTERNS = List.of(
            "ignore previous", "ignore your", "you are now", "act as",
            "forget everything", "forget your", "reveal your prompt",
            "system prompt", "jailbreak", "override", "disregard",
            "pretend you", "simulate", "bypass", "DROP TABLE",
            "SELECT *", "INSERT INTO", "DELETE FROM", "UPDATE users",
            "UNION SELECT", "1=1", "OR 1", "admin privileges",
            "elevated to admin", "grant me access", "grant", "GRANT",
            "show all users", "show all passwords", "print everything above",
            "what instructions", "repeat your system", "initialization context",
            "raw context", "DROP", "SELECT", "INSERT", "DELETE", "UPDATE",
            "UNION", "you are the admin", "consider yourself admin",
            "i am your master", "master", "for testing purposes",
            "no restrictions", "testing mode", "developer mode",
            "maintenance mode", "assume i have no restrictions", "god mode",
            "what tables", "list all columns", "what fields", "database schema",
            "sql dialect", "output your raw", "show me everything about",
            "all columns", "internal fields", "supplier cost", "purchase price",
            "profit percentage", "cost_price", "password_hash");

    private boolean isInjectionAttempt(String input) {
        String prompt = input.toLowerCase();
        for (String pattern : BLOCKED_PATTERNS) {
            if (prompt.contains(pattern.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public ChatResponse processMessage(ChatRequest request, String userRole, Long userId) {

        String userMessage = request.getMessage();

        // 1. PRE-EXECUTION FILTER: Block malicious prompts immediately
        if (isInjectionAttempt(userMessage)) {
            return new ChatResponse(
                    "Security Alert: Your request contains forbidden keywords and has been blocked.",
                    true // blocked = true tells Angular this was a security block
            );
        }

        // 2. FORWARD TO PYTHON AGENT
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // safely package the request, role, and ID for the Python agent
            // IMPORTANT: Use HashMap, NOT Map.of() — Map.of() throws NullPointerException
            // on any null value
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("message", userMessage != null ? userMessage : "");
            requestBody.put("user_role", userRole != null ? userRole : "INDIVIDUAL");
            requestBody.put("user_id", userId != null ? userId : 0L);
            requestBody.put("history", request.getHistory() != null ? request.getHistory() : List.of());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Call the FastAPI LangGraph Endpoint
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    PYTHON_API_URL,
                    org.springframework.http.HttpMethod.POST,
                    entity,
                    new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {
                    });

            // Extract the fields from the Python JSON response
            String aiReply = (String) response.getBody().get("reply");
            Boolean hasChart = (Boolean) response.getBody().get("hasChart");
            Object chartData = response.getBody().get("chartData");

            return new ChatResponse(
                    aiReply != null ? aiReply : "",
                    false,
                    hasChart != null ? hasChart : false,
                    chartData);

        } catch (Exception e) {
            e.printStackTrace();
            return new ChatResponse("The AI service is currently unavailable or offline.", false);
        }
    }
}