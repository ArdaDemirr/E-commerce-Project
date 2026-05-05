package com.advanced.projectspring.controllers.Corporate;

import com.advanced.projectspring.dto.corporate.CorporateShipmentResponseDTO;
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
@RequestMapping("/api/corporate/shipments")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PreAuthorize("hasRole('CORPORATE')")
public class CorporateShipmentController {

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

    @PostMapping
    public ResponseEntity<CorporateShipmentResponseDTO> createShipment(@RequestBody Long orderId,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        CorporateShipmentResponseDTO created = shipmentService.addShipment(orderId, userId);
        return new ResponseEntity<>(created, HttpStatus.CREATED); // Returns 201 Created for Postman
    }

    @GetMapping
    public ResponseEntity<List<CorporateShipmentResponseDTO>> getMyStoreShipments(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(shipmentService.getShipmentsForStoreOwner(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorporateShipmentResponseDTO> getShipmentById(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long userId = getUserId(request);
        return ResponseEntity.ok(shipmentService.getShipmentById(id, userId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CorporateShipmentResponseDTO> updateShipmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload,
            HttpServletRequest request) {

        Long userId = getUserId(request);
        String newStatus = payload.get("status");

        return ResponseEntity.ok(shipmentService.updateShipmentStatus(id, newStatus, userId));
    }
}
