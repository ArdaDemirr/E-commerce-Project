package com.advanced.projectspring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.CustomerProfile;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    Optional<CustomerProfile> findByUserId(Long userId);
    // SELECT * FROM customer_profiles WHERE user_id = ?
    // Optional means it might return null if not found — safer than returning null
    // directly
}
