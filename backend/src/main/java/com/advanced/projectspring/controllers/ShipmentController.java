package com.advanced.projectspring.controllers;

import com.advanced.projectspring.dto.individual.Shipment.ShipmentResponseDTO;
import com.advanced.projectspring.services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

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

    // ==========================================
    // PUBLIC / INDIVIDUAL ENDPOINTS
    // ==========================================

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

    // ==========================================
    // CORPORATE ENDPOINTS
    // ==========================================

    @PostMapping("/create")
    @PreAuthorize("hasRole('CORPORATE')")
    public ResponseEntity<ShipmentResponseDTO> createShipment(@RequestBody Long orderId, HttpServletRequest request) {
        Long userId = getUserId(request);
        ShipmentResponseDTO created = shipmentService.addShipment(orderId, userId);
        return new ResponseEntity<>(created, HttpStatus.CREATED); // Returns 201 Created for Postman
    }

    @GetMapping("/corporate")
    @PreAuthorize("hasRole('CORPORATE')")
    public ResponseEntity<List<ShipmentResponseDTO>> getMyStoreShipments(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(shipmentService.getShipmentsForStoreOwner(userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('CORPORATE')")
    public ResponseEntity<ShipmentResponseDTO> updateShipmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            HttpServletRequest request) {

        Long userId = getUserId(request);
        String newStatus = payload.get("status");

        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, newStatus, userId));
    }
}