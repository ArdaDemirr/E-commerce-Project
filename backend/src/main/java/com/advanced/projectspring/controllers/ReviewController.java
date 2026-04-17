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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // @Autowired
    // private JwtUtil jwtUtil;

    // CREATE REVIEW
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> addReview(
            @Valid @RequestBody ReviewRequestDTO requestDTO,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(reviewService.addReview(email, requestDTO));
    }

    // UPDATE REVIEW
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(@PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO requestDTO,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(reviewService.updateReview(id, email, requestDTO));
    }

    // DELETE REVIEW
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        reviewService.deleteReview(id, email);
        return ResponseEntity.noContent().build();
    }

    // GET ONLY MY REVIEWS
    @GetMapping("/my-reviews")
    public ResponseEntity<List<MyReviewDTO>> getMyReviews(
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(reviewService.getUserReviews(email));
    }

    @GetMapping("/my-reviews/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(
            @PathVariable Long id,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return ResponseEntity.ok(reviewService.getReviewById(id, email));
    }

    // GET ONLY PRODUCT REVIEWS
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReviewDTO>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }
}