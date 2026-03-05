package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId); // return a list of order items for a specific order

    List<OrderItem> findByProductId(Long productId); // return a list of order items for a specific product
}
