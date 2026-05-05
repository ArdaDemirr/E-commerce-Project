package com.advanced.projectspring.models;

import jakarta.persistence.*;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String warehouse;
    private String mode;
    private String status;
    private String trackingId;
    private Integer customerCareCalls;
    private Integer customerRating;
    private String productImportance;
    private Integer discountOffered;

    // Constructors
    public Shipment() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public String getMode() {
        return mode;
    }

    public String getStatus() {
        return status;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Integer getCustomerCareCalls() {
        return customerCareCalls;
    }

    public Integer getCustomerRating() {
        return customerRating;
    }

    public String getProductImportance() {
        return productImportance;
    }

    public Integer getDiscountOffered() {
        return discountOffered;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public void setCustomerCareCalls(Integer customerCareCalls) {
        this.customerCareCalls = customerCareCalls;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public void setProductImportance(String productImportance) {
        this.productImportance = productImportance;
    }

    public void setDiscountOffered(Integer discountOffered) {
        this.discountOffered = discountOffered;
    }
}
