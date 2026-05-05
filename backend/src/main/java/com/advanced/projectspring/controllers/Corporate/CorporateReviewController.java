package com.advanced.projectspring.controllers.Corporate;

import com.advanced.projectspring.dto.individual.Review.ProductReviewDTO;
import com.advanced.projectspring.services.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/corporate/reviews")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PreAuthorize("hasRole('CORPORATE')")
public class CorporateReviewController {

    @Autowired
    private ReviewService reviewService;

    private Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw new org.springframework.security.access.AccessDeniedException("User ID not found in token");
        }
        return (Long) userId;
    }

    // GET REVIEWS FOR STORE PRODUCTS
    @GetMapping
    public ResponseEntity<List<ProductReviewDTO>> getStoreReviews(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(reviewService.getStoreOwnerReviews(userId));
    }
}
