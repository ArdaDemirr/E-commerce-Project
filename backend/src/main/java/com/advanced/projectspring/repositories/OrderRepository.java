package com.advanced.projectspring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // individual user's "My Orders" page — shows only their orders
    List<Order> findByUserId(Long userId); // return a list of orders for a specific user

    // corporate sees all orders coming to their store
    List<Order> findByStoreId(Long storeId); // return a list of orders for a specific store

    // filter orders by pending/shipped/delivered/cancelled
    List<Order> findByStatus(String status); // return a list of orders with a specific status

    // corporate filters their store's orders by status. Example: "show me all
    // pending orders for my store"
    List<Order> findByStoreIdAndStatus(Long storeId, String status); // return a list of orders for a specific store

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    // methods for store owner
    List<Order> findByStoreOwnerId(Long ownerId);

    Optional<Order> findByIdAndStoreOwnerId(Long id, Long ownerId);
}
