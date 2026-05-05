package com.advanced.projectspring.controllers;

import com.advanced.projectspring.dto.AnalyticsResponse;
import com.advanced.projectspring.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin-only analytics endpoints.
 * All routes are protected by @PreAuthorize("hasRole('ADMIN')") — Spring
 * Security
 * rejects any non-admin token before the method body runs.
 */
@RestController
@RequestMapping("/api/admin/analytics")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * GET /api/admin/analytics/dashboard
     * Full KPI dashboard — total orders, revenue, recent orders, best sellers, etc.
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnalyticsResponse> getDashboard() {
        return ResponseEntity.ok(analyticsService.getDashboard());
    }

    /**
     * GET /api/admin/analytics/recent-orders?limit=5
     * Returns the last N orders across the platform.
     */
    @GetMapping("/recent-orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getRecentOrders(limit));
    }

    /**
     * GET /api/admin/analytics/best-sellers?limit=5
     * Returns the top N best-selling products by total units sold.
     */
    @GetMapping("/best-sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getBestSellers(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(analyticsService.getBestSellers(limit));
    }
}
