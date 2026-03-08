package com.advanced.projectspring.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // a order can be belong to single user but an user can have many orders
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // use user_id as foreign key
    private User user; // use joincolumn to link them

    // a order can be belong to single store but an store can have many orders
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false) // use store_id as foreign key
    private Store store; // use joincolumn to link them

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Double grandTotal;

    private String paymentMethod;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Order() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Store getStore() {
        return store;
    }

    public String getStatus() {
        return status;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
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

    public void setStore(Store store) {
        this.store = store;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}