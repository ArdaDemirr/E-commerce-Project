package com.advanced.projectspring.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer starRating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private String sentiment;
    private Integer helpfulVotes;
    private Integer totalVotes;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Review() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getStarRating() {
        return starRating;
    }

    public String getComment() {
        return comment;
    }

    public String getSentiment() {
        return sentiment;
    }

    public Integer getHelpfulVotes() {
        return helpfulVotes;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setStarRating(Integer starRating) {
        this.starRating = starRating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public void setHelpfulVotes(Integer helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}