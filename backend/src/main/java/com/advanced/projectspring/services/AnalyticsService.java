package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.AnalyticsResponse;
import com.advanced.projectspring.models.Order;
import com.advanced.projectspring.models.Product;
import com.advanced.projectspring.repositories.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    public AnalyticsResponse getDashboard() {
        AnalyticsResponse response = new AnalyticsResponse();

        // -- KPIs --
        response.setTotalOrders(analyticsRepository.findTotalOrderCount());
        response.setOrdersThisMonth(analyticsRepository.findOrdersThisMonth());
        response.setTotalRevenue(analyticsRepository.findTotalRevenue());

        // -- Recent Orders (last 10) --
        List<Order> recentOrders = analyticsRepository.findRecentOrders(PageRequest.of(0, 10));
        List<Map<String, Object>> recentOrderList = new ArrayList<>();
        for (Order o : recentOrders) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", o.getId());
            map.put("status", o.getStatus());
            map.put("grandTotal", o.getGrandTotal());
            map.put("customerEmail", o.getUser() != null ? o.getUser().getEmail() : "N/A");
            map.put("store", o.getStore() != null ? o.getStore().getName() : "N/A");
            map.put("createdAt", o.getCreatedAt());
            recentOrderList.add(map);
        }
        response.setRecentOrders(recentOrderList);

        // -- Best-selling Products (top 5) --
        List<Object[]> bestSellers = analyticsRepository.findBestSellingProducts(PageRequest.of(0, 5));
        List<Map<String, Object>> bestSellerList = new ArrayList<>();
        for (Object[] row : bestSellers) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("productId", row[0]);
            map.put("productName", row[1]);
            map.put("unitsSold", row[2]);
            bestSellerList.add(map);
        }
        response.setBestSellingProducts(bestSellerList);

        // -- Most Expensive Products (top 5) --
        List<Product> expensiveProducts = analyticsRepository.findMostExpensiveProducts(PageRequest.of(0, 5));
        List<Map<String, Object>> expensiveList = new ArrayList<>();
        for (Product p : expensiveProducts) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("productId", p.getId());
            map.put("productName", p.getName());
            map.put("price", p.getUnitPrice());
            map.put("store", p.getStore() != null ? p.getStore().getName() : "N/A");
            expensiveList.add(map);
        }
        response.setMostExpensiveProducts(expensiveList);

        // -- Revenue By Store --
        List<Object[]> revByStore = analyticsRepository.findRevenueByStore();
        List<Map<String, Object>> storeRevList = new ArrayList<>();
        for (Object[] row : revByStore) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("storeId", row[0]);
            map.put("storeName", row[1]);
            map.put("totalRevenue", row[2]);
            storeRevList.add(map);
        }
        response.setRevenueByStore(storeRevList);

        // -- Orders By Status --
        List<Object[]> byStatus = analyticsRepository.findOrderCountByStatus();
        Map<String, Long> statusMap = new LinkedHashMap<>();
        for (Object[] row : byStatus) {
            statusMap.put((String) row[0], (Long) row[1]);
        }
        response.setOrdersByStatus(statusMap);

        return response;
    }

    /** Quick lookup of the most recent N orders (for chatbot to call) */
    public List<Map<String, Object>> getRecentOrders(int limit) {
        List<Order> orders = analyticsRepository.findRecentOrders(PageRequest.of(0, limit));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Order o : orders) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", o.getId());
            map.put("status", o.getStatus());
            map.put("grandTotal", o.getGrandTotal());
            map.put("customer", o.getUser() != null ? o.getUser().getEmail() : "N/A");
            map.put("store", o.getStore() != null ? o.getStore().getName() : "N/A");
            map.put("createdAt", o.getCreatedAt());
            result.add(map);
        }
        return result;
    }

    /** Top N best-selling products */
    public List<Map<String, Object>> getBestSellers(int limit) {
        List<Object[]> rows = analyticsRepository.findBestSellingProducts(PageRequest.of(0, limit));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("productId", row[0]);
            map.put("productName", row[1]);
            map.put("unitsSold", row[2]);
            result.add(map);
        }
        return result;
    }
}
