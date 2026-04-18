package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.individual.Order.OrderItemRequestDTO;
import com.advanced.projectspring.dto.individual.Order.OrderItemResponseDTO;
import com.advanced.projectspring.dto.individual.Order.OrderRequestDTO;
import com.advanced.projectspring.dto.individual.Order.OrderResponseDTO;
import com.advanced.projectspring.dto.corporate.CorporateOrderResponseDTO;
import com.advanced.projectspring.dto.CustomerSummaryDTO;
import com.advanced.projectspring.dto.StoreSummaryDTO;
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
    @Autowired
    private ShipmentService shipmentService;

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setGrandTotal(order.getGrandTotal());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setStatus(order.getStatus());
        dto.setStore(new StoreSummaryDTO(order.getStore().getId(), order.getStore().getName(),
                order.getStore().getStatus()));

        List<OrderItemResponseDTO> itemDTOs = orderItemRepository.findByOrderId(order.getId()).stream().map(item -> {
            OrderItemResponseDTO itemDto = new OrderItemResponseDTO();
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getPrice());
            itemDto.setTotalPrice(item.getQuantity() * item.getPrice());
            return itemDto;
        }).toList();

        dto.setItems(itemDTOs);
        return dto;
    }

    @Transactional
    public OrderResponseDTO placeOrder(Long userId, OrderRequestDTO request) {
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

        if (savedOrder.getStore() != null && savedOrder.getStore().getOwner() != null) {
            shipmentService.addShipment(savedOrder.getId(), savedOrder.getStore().getOwner().getId());
        }

        // 2. Process each item in the cart
        for (OrderItemRequestDTO itemReq : request.getItems()) {
            Product product = productRepository.findByIdWithLock(itemReq.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStock() == null || product.getStock() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Product is out of stock or invalid: " + product.getName());
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
        Order finalOrder = orderRepository.save(savedOrder);

        return mapToOrderResponseDTO(finalOrder);
    }

    public List<OrderResponseDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToOrderResponseDTO)
                .toList();
    }

    public OrderResponseDTO getOrderById(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .map(this::mapToOrderResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Order not found or access denied"));
    }

    private CorporateOrderResponseDTO mapToCorporateOrderResponseDTO(Order order) {
        CorporateOrderResponseDTO dto = new CorporateOrderResponseDTO();
        dto.setId(order.getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setGrandTotal(order.getGrandTotal());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setStatus(order.getStatus());
        if (order.getUser() != null) {
            dto.setCustomer(new CustomerSummaryDTO(order.getUser().getId(), order.getUser().getName(),
                    order.getUser().getSurname(), order.getUser().getEmail()));
        } else {
            dto.setCustomer(null);
        }

        List<OrderItemResponseDTO> itemDTOs = orderItemRepository.findByOrderId(order.getId()).stream().map(item -> {
            OrderItemResponseDTO itemDto = new OrderItemResponseDTO();
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getPrice());
            itemDto.setTotalPrice(item.getQuantity() * item.getPrice());
            return itemDto;
        }).toList();

        dto.setItems(itemDTOs);
        return dto;
    }

    // STORE ORDERS
    public List<CorporateOrderResponseDTO> getStoreOrders(Long userId) {
        return orderRepository.findByStoreOwnerId(userId).stream()
                .map(this::mapToCorporateOrderResponseDTO)
                .toList();
    }

    public CorporateOrderResponseDTO getStoreOrderById(Long orderId, Long userId) {
        return orderRepository.findByIdAndStoreOwnerId(orderId, userId)
                .map(this::mapToCorporateOrderResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Order not found or access denied"));
    }
}
