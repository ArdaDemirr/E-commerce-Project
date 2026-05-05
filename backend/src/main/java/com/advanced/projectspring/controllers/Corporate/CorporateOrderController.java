package com.advanced.projectspring.controllers.Corporate;

import com.advanced.projectspring.dto.corporate.CorporateOrderResponseDTO;
import com.advanced.projectspring.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/corporate/orders")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PreAuthorize("hasRole('CORPORATE')")
public class CorporateOrderController {

    @Autowired
    private OrderService orderService;

    // GET ONLY STORE ORDERS
    @GetMapping
    public ResponseEntity<List<CorporateOrderResponseDTO>> getStoreOrders(
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getStoreOrders(userId));
    }

    // GET SINGLE ORDER WITH ID, users can only get their own orders
    @GetMapping("/{id}")
    public ResponseEntity<CorporateOrderResponseDTO> getOrderById(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getStoreOrderById(id, userId));
    }
}
