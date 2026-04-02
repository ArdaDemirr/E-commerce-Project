package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.individual.OrderRequestDTO;
import com.advanced.projectspring.models.*;
import com.advanced.projectspring.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(Long userId, OrderRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        double grandTotal = 0.0;

        // 1. Create the main Order header
        Order order = new Order();
        order.setUser(user);
        order.setStore(store);
        order.setStatus("Pending");
        order.setPaymentMethod(request.getPaymentMethod());
        order.setGrandTotal(0.0); // Temporary, we will calculate below
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // 2. Process each item in the cart
        for (OrderRequestDTO.OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStock() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for: " + product.getName());
            }

            // Deduct stock
            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);

            // Create OrderItem record
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(product.getUnitPrice());
            orderItemRepository.save(orderItem);

            grandTotal += (product.getUnitPrice() * itemReq.getQuantity());
        }

        // 3. Update the final total and save again
        savedOrder.setGrandTotal(grandTotal);
        return orderRepository.save(savedOrder);
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
