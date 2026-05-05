package com.advanced.projectspring.dto.admin;

public class StoreRankingDTO {
    private Long storeId;
    private String storeName;
    private Double totalRevenue;
    private Long orderCount;

    public StoreRankingDTO(Long storeId, String storeName, Double totalRevenue, Long orderCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalRevenue = totalRevenue;
        this.orderCount = orderCount;
    }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Long getOrderCount() { return orderCount; }
    public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }
}
