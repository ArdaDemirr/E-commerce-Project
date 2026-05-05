package com.advanced.projectspring.controllers.Corporate;

import com.advanced.projectspring.dto.corporate.CorporateAnalyticsDTO;
import com.advanced.projectspring.services.CorporateAnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/corporate/analytics")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PreAuthorize("hasRole('CORPORATE')")
public class CorporateAnalyticsController {

    @Autowired
    private CorporateAnalyticsService corporateAnalyticsService;

    // userId is extracted from the JWT cookie by JwtAuthFilter and set as a request
    // attribute.
    private Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw new org.springframework.security.access.AccessDeniedException("User ID not found in token");
        }
        return (Long) userId;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<CorporateAnalyticsDTO> getDashboardAnalytics(HttpServletRequest request) {
        Long userId = getUserId(request);
        CorporateAnalyticsDTO analytics = corporateAnalyticsService.getDashboardAnalytics(userId);
        return ResponseEntity.ok(analytics);
    }
}
