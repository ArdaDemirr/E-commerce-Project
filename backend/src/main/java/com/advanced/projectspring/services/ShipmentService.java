package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.individual.ShipmentResponseDTO;
import com.advanced.projectspring.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    public ShipmentResponseDTO getShipmentByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId).map(shipment -> {
            ShipmentResponseDTO dto = new ShipmentResponseDTO();
            // Matching the exact getters from your Shipment.java model
            dto.setTrackingId(shipment.getTrackingId());
            dto.setMode(shipment.getMode());
            dto.setStatus(shipment.getStatus());
            dto.setWarehouse(shipment.getWarehouse());
            dto.setProductImportance(shipment.getProductImportance());
            dto.setOrderId(shipment.getOrder().getId());
            return dto;
        }).orElse(null);
    }
}