package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // when user clicks on an order to see details, we fetch all items in that order
    List<OrderItem> findByOrderId(Long orderId); // return a list of order items for a specific order

    // corporate analytics — "how many times was this product ordered?"
    List<OrderItem> findByProductId(Long productId); // return a list of order items for a specific product
}
