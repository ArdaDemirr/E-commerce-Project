package com.advanced.projectspring.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsResponse {

    private Long totalOrders;
    private Long ordersThisMonth;
    private Double totalRevenue;
    private List<Map<String, Object>> recentOrders;
    private List<Map<String, Object>> bestSellingProducts;
    private List<Map<String, Object>> mostExpensiveProducts;
    private List<Map<String, Object>> revenueByStore;
    private Map<String, Long> ordersByStatus;

    // ------- Getters & Setters -------

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getOrdersThisMonth() {
        return ordersThisMonth;
    }

    public void setOrdersThisMonth(Long ordersThisMonth) {
        this.ordersThisMonth = ordersThisMonth;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<Map<String, Object>> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<Map<String, Object>> recentOrders) {
        this.recentOrders = recentOrders;
    }

    public List<Map<String, Object>> getBestSellingProducts() {
        return bestSellingProducts;
    }

    public void setBestSellingProducts(List<Map<String, Object>> bestSellingProducts) {
        this.bestSellingProducts = bestSellingProducts;
    }

    public List<Map<String, Object>> getMostExpensiveProducts() {
        return mostExpensiveProducts;
    }

    public void setMostExpensiveProducts(List<Map<String, Object>> mostExpensiveProducts) {
        this.mostExpensiveProducts = mostExpensiveProducts;
    }

    public List<Map<String, Object>> getRevenueByStore() {
        return revenueByStore;
    }

    public void setRevenueByStore(List<Map<String, Object>> revenueByStore) {
        this.revenueByStore = revenueByStore;
    }

    public Map<String, Long> getOrdersByStatus() {
        return ordersByStatus;
    }

    public void setOrdersByStatus(Map<String, Long> ordersByStatus) {
        this.ordersByStatus = ordersByStatus;
    }
}
