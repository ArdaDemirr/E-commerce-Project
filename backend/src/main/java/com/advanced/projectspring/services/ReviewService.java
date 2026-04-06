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

        if (request.getRating() >= 4) {
            review.setSentiment("positive");
        } else if (request.getRating() == 3) {
            review.setSentiment("neutral");
        } else {
            review.setSentiment("negative");
        }

        Review savedReview = reviewRepository.save(review);

        ReviewResponseDTO response = new ReviewResponseDTO();
        response.setId(savedReview.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setRating(savedReview.getStarRating());
        response.setComment(savedReview.getComment());
        response.setSentiment(savedReview.getSentiment());
        response.setCreatedAt(savedReview.getCreatedAt());

        return response;
    }

    // VERSION 1: User Specific (For the Yorumlarım tab)
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

    // VERSION 2: Global/Product Specific (For the Product Details page)
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
}