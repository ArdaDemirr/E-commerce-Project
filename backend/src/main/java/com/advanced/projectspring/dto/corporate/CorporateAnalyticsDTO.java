package com.advanced.projectspring.dto.corporate;

public class CorporateAnalyticsDTO {
    private double totalEarnings;
    private long totalOrders;
    private long totalProducts;

    public CorporateAnalyticsDTO() {
    }

    public CorporateAnalyticsDTO(double totalEarnings, long totalOrders, long totalProducts) {
        this.totalEarnings = totalEarnings;
        this.totalOrders = totalOrders;
        this.totalProducts = totalProducts;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }
}
