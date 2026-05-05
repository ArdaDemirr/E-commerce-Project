/*
 * Vertical Partitioning - splitting the user table into multiple tables
 * CustomerProfile table - contains additional information about the user
 * think like extension of the user table
 */

package com.advanced.projectspring.models;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one-to-one relationship with User - link the user directly
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer age;
    private String city;
    private String membershipType;
    private Double totalSpend;
    private Integer itemsPurchased;
    private Double avgRating;
    private String satisfactionLevel;

    // Constructors
    public CustomerProfile() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Integer getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public Double getTotalSpend() {
        return totalSpend;
    }

    public Integer getItemsPurchased() {
        return itemsPurchased;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public String getSatisfactionLevel() {
        return satisfactionLevel;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public void setTotalSpend(Double totalSpend) {
        this.totalSpend = totalSpend;
    }

    public void setItemsPurchased(Integer itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public void setSatisfactionLevel(String satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
    }
}
