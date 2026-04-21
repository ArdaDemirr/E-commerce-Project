package com.advanced.projectspring.controllers.Admin;

import com.advanced.projectspring.dto.corporate.CorporateOrderResponseDTO;
import com.advanced.projectspring.dto.corporate.CorporateShipmentResponseDTO;
import com.advanced.projectspring.dto.individual.Review.ReviewResponseDTO;
import com.advanced.projectspring.services.OrderService;
import com.advanced.projectspring.services.ReviewService;
import com.advanced.projectspring.services.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/traffic")
public class AdminTrafficController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CorporateOrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/shipments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CorporateShipmentResponseDTO>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
}
