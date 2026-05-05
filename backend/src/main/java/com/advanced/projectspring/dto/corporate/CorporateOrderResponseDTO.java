package com.advanced.projectspring.dto.corporate;

import com.advanced.projectspring.dto.CustomerSummaryDTO;
import com.advanced.projectspring.dto.individual.Order.OrderItemResponseDTO;
import java.time.LocalDateTime;
import java.util.List;

public class CorporateOrderResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Double grandTotal;
    private String paymentMethod;
    private String status;
    private CustomerSummaryDTO customer; // This explicitly informs store owner who ordered
    private List<OrderItemResponseDTO> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerSummaryDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerSummaryDTO customer) {
        this.customer = customer;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }
}
