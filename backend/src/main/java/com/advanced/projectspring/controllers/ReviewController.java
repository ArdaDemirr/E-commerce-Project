package com.advanced.projectspring.controllers;

//import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.individual.Review.ProductReviewDTO;
import com.advanced.projectspring.dto.individual.Review.MyReviewDTO;
import com.advanced.projectspring.dto.individual.Review.ReviewRequestDTO;
import com.advanced.projectspring.dto.individual.Review.ReviewResponseDTO;

import com.advanced.projectspring.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // @Autowired
    // private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> addReview(
            @RequestBody ReviewRequestDTO requestDTO,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(reviewService.addReview(email, requestDTO));
    }

    // Endpoint for Yorumlarım tab
    @GetMapping("/my-reviews")
    public ResponseEntity<List<MyReviewDTO>> getMyReviews(
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(reviewService.getUserReviews(email));
    }

    // Endpoint for Product Details page
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReviewDTO>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }
}