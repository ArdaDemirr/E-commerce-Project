package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.corporate.CorporateAnalyticsDTO;
import com.advanced.projectspring.models.Order;
import com.advanced.projectspring.models.Product;
import com.advanced.projectspring.repositories.OrderRepository;
import com.advanced.projectspring.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorporateAnalyticsService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public CorporateAnalyticsDTO getDashboardAnalytics(Long userId) {
        List<Order> orders = orderRepository.findByStoreOwnerId(userId);
        List<Product> products = productRepository.findByStoreOwnerId(userId);

        long totalOrders = orders.size();
        
        double totalEarnings = orders.stream()
                .mapToDouble(o -> o.getGrandTotal() != null ? o.getGrandTotal() : 0.0)
                .sum();

        long totalProducts = products.size();

        return new CorporateAnalyticsDTO(totalEarnings, totalOrders, totalProducts);
    }
}
