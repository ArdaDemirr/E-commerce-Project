/*
 * Backend'den Angular'a gönderilen Order nesnesini temsil eder.
 * içinde anguların ihtiyacı olduğu bilgiler bulunur, angulara direkt raw database objesini göndermekten çekiniyoruz ki
 * bir yerlerde data leak olmasın, her şey kapsüller halinde iletilmeli.
 */

package com.advanced.projectspring.dto.individual.Order;

import java.time.LocalDateTime;
import java.util.List;

import com.advanced.projectspring.dto.StoreSummaryDTO;

public class OrderResponseDTO {
    private Long id; // order id
    private LocalDateTime createdAt; // order date
    private Double grandTotal; // total price
    private String paymentMethod; // payment method
    private String status; // order status
    private StoreSummaryDTO store; // store info
    private List<OrderItemResponseDTO> items; // items in the order

    // Getters and Setters
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

    // store info
    public StoreSummaryDTO getStore() {
        return store;
    }

    public void setStore(StoreSummaryDTO store) {
        this.store = store;
    }

    // items in the order
    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }
}
