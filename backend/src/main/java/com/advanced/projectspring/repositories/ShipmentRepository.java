package com.advanced.projectspring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    // individual user tracks their shipment. Each order has one shipment
    Optional<Shipment> findByOrderId(Long orderId);

    // corporate sees all shipments by status (pending/in_transit/delivered)
    List<Shipment> findByStatus(String status);
}
