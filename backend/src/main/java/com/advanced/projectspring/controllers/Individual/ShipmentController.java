package com.advanced.projectspring.controllers.Individual;

import com.advanced.projectspring.dto.individual.Shipment.ShipmentResponseDTO;
import com.advanced.projectspring.services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    // Helper to safely extract User ID
    private Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw new org.springframework.security.access.AccessDeniedException("User ID not found in token");
        }
        return (Long) userId;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponseDTO> getTrackingInfo(@PathVariable Long orderId, HttpServletRequest request) {
        Long userId = getUserId(request);
        String role = (String) request.getAttribute("role");

        ShipmentResponseDTO shipment = shipmentService.getShipmentByOrderId(orderId, userId, role);

        if (shipment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shipment);
    }
}