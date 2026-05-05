package com.advanced.projectspring.dto.admin;

public class CustomerRankingDTO {
    private Long userId;
    private String name;
    private String surname;
    private String email;
    private Double totalSpent;
    private Long orderCount;

    public CustomerRankingDTO(Long userId, String name, String surname, String email, Double totalSpent, Long orderCount) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.totalSpent = totalSpent;
        this.orderCount = orderCount;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(Double totalSpent) { this.totalSpent = totalSpent; }

    public Long getOrderCount() { return orderCount; }
    public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }
}
