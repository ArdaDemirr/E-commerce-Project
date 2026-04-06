package com.advanced.projectspring.controllers;

import com.advanced.projectspring.dto.individual.Shipment.ShipmentResponseDTO;
import com.advanced.projectspring.services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "http://localhost:4200")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;
    // injects the shipment service

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponseDTO> getTrackingInfo(@PathVariable Long orderId) {
        ShipmentResponseDTO shipment = shipmentService.getShipmentByOrderId(orderId);
        // calls the shipment service to get the shipment response dto
        // if the shipment is null, returns 404
        // otherwise returns the shipment response dto

        if (shipment == null) {
            // Return 404 if the order hasn't been shipped yet
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shipment);
    }
}