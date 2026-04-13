package com.advanced.projectspring.controllers;

//import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.individual.Order.OrderRequestDTO;
import com.advanced.projectspring.dto.individual.Order.OrderResponseDTO;
import com.advanced.projectspring.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // @Autowired
    // private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @RequestBody OrderRequestDTO requestDTO,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        OrderResponseDTO createdOrder = orderService.placeOrder(userId, requestDTO);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }
}
