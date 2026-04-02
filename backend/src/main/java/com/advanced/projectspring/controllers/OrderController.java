package com.advanced.projectspring.controllers;

import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.individual.OrderRequestDTO;
import com.advanced.projectspring.models.Order;
import com.advanced.projectspring.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestBody OrderRequestDTO request,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = jwtUtil.extractUserId(authHeader.substring(7));
        Order createdOrder = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = jwtUtil.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }
}
