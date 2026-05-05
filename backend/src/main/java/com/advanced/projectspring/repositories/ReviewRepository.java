package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // product detail page shows all reviews for that product
    List<Review> findByProductId(Long productId);

    // user's profile shows reviews they wrote
    List<Review> findByUserId(Long userId);

    // filter reviews by 1-5 stars
    List<Review> findByStarRating(Integer starRating);

    // this is important for AI. When chatbot is asked "show me negative reviews for
    // this product" we call findBySentiment("negative")
    List<Review> findBySentiment(String sentiment);

    // used by store owners to view reviews made on their specific products
    List<Review> findByProductStoreOwnerId(Long ownerId);
}
