package com.advanced.projectspring.controllers.Individual;

import com.advanced.projectspring.dto.individual.Order.OrderRequestDTO;
import com.advanced.projectspring.dto.individual.Order.OrderResponseDTO;
import com.advanced.projectspring.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // CREATE ORDER
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @Valid @RequestBody OrderRequestDTO requestDTO,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        OrderResponseDTO createdOrder = orderService.placeOrder(userId, requestDTO);
        return ResponseEntity.ok(createdOrder);
    }

    // GET ONLY MY ORDERS
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    // GET SINGLE ORDER WITH ID, users can only get their own orders
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getOrderById(id, userId));
    }
}
