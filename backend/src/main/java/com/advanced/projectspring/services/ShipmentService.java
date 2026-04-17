package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.individual.Shipment.ShipmentResponseDTO;
import com.advanced.projectspring.repositories.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository; // inject the repository

    public ShipmentResponseDTO getShipmentByOrderId(Long orderId, Long userId, String role) {
        return shipmentRepository.findByOrderId(orderId)
                .filter(shipment -> {
                    boolean isOwner = userId != null && userId.equals(shipment.getOrder().getUser().getId());
                    boolean isAdmin = "ADMIN".equals(role);
                    boolean isStoreOwner = userId != null
                            && userId.equals(shipment.getOrder().getStore().getOwner().getId());
                    return isOwner || isAdmin || isStoreOwner;
                })
                .map(shipment -> {
                    ShipmentResponseDTO dto = new ShipmentResponseDTO();
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