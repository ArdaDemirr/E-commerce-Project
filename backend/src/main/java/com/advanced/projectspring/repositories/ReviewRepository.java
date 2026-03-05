package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);

    List<Review> findByStarRating(Integer starRating);

    List<Review> findBySentiment(String sentiment);
}
