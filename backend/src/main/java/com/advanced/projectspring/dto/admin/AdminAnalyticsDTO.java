package com.advanced.projectspring.dto.admin;

import java.util.List;

public class AdminAnalyticsDTO {
    private List<StoreRankingDTO> topStores;
    private List<CustomerRankingDTO> topCustomers;
    private Double totalRevenue;
    private Long totalOrders;
    private Long totalReviews;
    private Long totalShipments;

    public AdminAnalyticsDTO(List<StoreRankingDTO> topStores, List<CustomerRankingDTO> topCustomers,
                              Double totalRevenue, Long totalOrders, Long totalReviews, Long totalShipments) {
        this.topStores = topStores;
        this.topCustomers = topCustomers;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.totalReviews = totalReviews;
        this.totalShipments = totalShipments;
    }

    public List<StoreRankingDTO> getTopStores() { return topStores; }
    public void setTopStores(List<StoreRankingDTO> topStores) { this.topStores = topStores; }

    public List<CustomerRankingDTO> getTopCustomers() { return topCustomers; }
    public void setTopCustomers(List<CustomerRankingDTO> topCustomers) { this.topCustomers = topCustomers; }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

    public Long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

    public Long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Long totalReviews) { this.totalReviews = totalReviews; }

    public Long getTotalShipments() { return totalShipments; }
    public void setTotalShipments(Long totalShipments) { this.totalShipments = totalShipments; }
}
