/*
 * Angulardan bize gelen Order nesnesini temsil eder.
 * içinde sadece storeId, paymentMethod ve items bulunur.
 * önemli olan hangi store, ne ile ödüyoruz, ne alıyoruz. Geriye kalan bilgiler güvenlik amacıyla backend tarafından belirlenir.
 */

package com.advanced.projectspring.dto.individual.Order;

import java.util.List;

public class OrderRequestDTO {
    private Long storeId;
    private String paymentMethod;
    private List<OrderItemRequestDTO> items;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDTO> items) {
        this.items = items;
    }
}
