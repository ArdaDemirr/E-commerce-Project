package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.individual.Review.ProductReviewDTO;
import com.advanced.projectspring.dto.individual.Review.MyReviewDTO;
import com.advanced.projectspring.dto.individual.Review.ReviewRequestDTO;
import com.advanced.projectspring.dto.individual.Review.ReviewResponseDTO;
import com.advanced.projectspring.models.Product;
import com.advanced.projectspring.models.Review;
import com.advanced.projectspring.models.User;
import com.advanced.projectspring.repositories.ProductRepository;
import com.advanced.projectspring.repositories.ReviewRepository;
import com.advanced.projectspring.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    // ==========================================
    // CORE METHODS
    // ==========================================

    public ReviewResponseDTO addReview(String email, ReviewRequestDTO request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setStarRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setHelpfulVotes(0);
        review.setTotalVotes(0);
        review.setSentiment(calculateSentiment(request.getRating()));

        Review savedReview = reviewRepository.save(review);
        return convertToResponseDTO(savedReview);
    }

    public List<MyReviewDTO> getUserReviews(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return reviewRepository.findByUserId(user.getId()).stream().map(review -> {
            MyReviewDTO dto = new MyReviewDTO();
            dto.setId(review.getId());
            dto.setProductId(review.getProduct().getId());
            dto.setProductName(review.getProduct().getName());
            dto.setRating(review.getStarRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            dto.setHelpfulVotes(review.getHelpfulVotes());
            dto.setSentiment(review.getSentiment());
            return dto;
        }).collect(Collectors.toList());
    }

    public ReviewResponseDTO getReviewById(Long reviewId, String userEmail) {
        Review review = getReviewAndVerifyOwnership(reviewId, userEmail);
        return convertToResponseDTO(review);
    }

    public List<ProductReviewDTO> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId).stream().map(review -> {
            ProductReviewDTO dto = new ProductReviewDTO();
            dto.setId(review.getId());

            String fullName = review.getUser().getName();
            if (review.getUser().getSurname() != null) {
                fullName += " " + review.getUser().getSurname();
            }
            dto.setReviewerName(fullName);
            dto.setRating(review.getStarRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            dto.setHelpfulVotes(review.getHelpfulVotes());
            dto.setSentiment(review.getSentiment());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ProductReviewDTO> getStoreOwnerReviews(Long ownerId) {
        return reviewRepository.findByProductStoreOwnerId(ownerId).stream().map(review -> {
            ProductReviewDTO dto = new ProductReviewDTO();
            dto.setId(review.getId());

            String fullName = review.getUser().getName();
            if (review.getUser().getSurname() != null) {
                fullName += " " + review.getUser().getSurname();
            }
            dto.setReviewerName(fullName);
            dto.setRating(review.getStarRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            dto.setHelpfulVotes(review.getHelpfulVotes());
            dto.setSentiment(review.getSentiment());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteReview(Long reviewId, String userEmail) {
        Review review = getReviewAndVerifyOwnership(reviewId, userEmail);
        reviewRepository.delete(review);
    }

    public ReviewResponseDTO updateReview(Long reviewId, String userEmail, ReviewRequestDTO requestDTO) {
        if (requestDTO.getRating() < 1 || requestDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Fetch and verify ownership using the helper
        Review review = getReviewAndVerifyOwnership(reviewId, userEmail);

        // Update fields
        review.setStarRating(requestDTO.getRating());
        review.setComment(requestDTO.getComment());
        review.setSentiment(calculateSentiment(requestDTO.getRating())); // Recalculate sentiment

        Review updatedReview = reviewRepository.save(review);
        return convertToResponseDTO(updatedReview);
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    /**
     * Fetches the review and ensures the requesting user is the actual owner.
     */
    private Review getReviewAndVerifyOwnership(Long reviewId, String userEmail) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (!review.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You do not have permission to modify this review");
        }
        return review;
    }

    /**
     * Converts a Review entity into a standard Response DTO.
     */
    private ReviewResponseDTO convertToResponseDTO(Review review) {
        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(review.getId());

        if (review.getProduct() != null) {
            response.setProductId(review.getProduct().getId());
            response.setProductName(review.getProduct().getName());
        }

        response.setRating(review.getStarRating());
        response.setComment(review.getComment());
        response.setSentiment(review.getSentiment());
        response.setCreatedAt(review.getCreatedAt());

        return response;
    }

    /**
     * Determines sentiment based on the 1-5 star rating.
     */
    private String calculateSentiment(int rating) {
        if (rating >= 4)
            return "positive";
        if (rating == 3)
            return "neutral";
        return "negative";
    }
}