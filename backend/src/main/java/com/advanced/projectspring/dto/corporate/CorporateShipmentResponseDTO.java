package com.advanced.projectspring.dto.corporate;

import com.advanced.projectspring.dto.CustomerSummaryDTO;

public class CorporateShipmentResponseDTO {
    private Long id;
    private String trackingId;
    private String mode;
    private String status;
    private String warehouse;
    private String productImportance;
    private Long orderId;
    private CustomerSummaryDTO customer; // Explicit recipient info for logistics

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getProductImportance() {
        return productImportance;
    }

    public void setProductImportance(String productImportance) {
        this.productImportance = productImportance;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public CustomerSummaryDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerSummaryDTO customer) {
        this.customer = customer;
    }
}
