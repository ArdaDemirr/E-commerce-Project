package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.individual.Shipment.ShipmentResponseDTO;
import com.advanced.projectspring.dto.corporate.CorporateShipmentResponseDTO;
import com.advanced.projectspring.dto.CustomerSummaryDTO;
import com.advanced.projectspring.models.Order;
import com.advanced.projectspring.models.Shipment;
import com.advanced.projectspring.repositories.OrderRepository;
import com.advanced.projectspring.repositories.ShipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ShipmentResponseDTO getShipmentByOrderId(Long orderId, Long userId, String role) {
        return shipmentRepository.findByOrderId(orderId)
                .filter(shipment -> canAccessShipment(shipment, userId, role))
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    @Transactional
    public CorporateShipmentResponseDTO addShipment(Long orderId, Long storeOwnerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!storeOwnerId.equals(order.getStore().getOwner().getId())) {
            throw new SecurityException("You do not have permission to fulfill this order");
        }

        if (shipmentRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalStateException("Shipment already exists for this order");
        }

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setStatus("PREPARING");
        shipment.setTrackingId("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        shipment.setWarehouse("Central Distribution");
        shipment.setMode("Road");
        shipment.setProductImportance("Standard");

        Shipment savedShipment = shipmentRepository.save(shipment);
        return convertToCorporateResponseDTO(savedShipment);
    }

    @Transactional
    public CorporateShipmentResponseDTO updateShipmentStatus(Long shipmentId, String newStatus, Long storeOwnerId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found"));

        // SECURITY CHECK: Verify Ownership before updating
        if (!storeOwnerId.equals(shipment.getOrder().getStore().getOwner().getId())) {
            throw new SecurityException("You do not have permission to update this shipment");
        }

        shipment.setStatus(newStatus);
        Shipment updatedShipment = shipmentRepository.save(shipment);
        return convertToCorporateResponseDTO(updatedShipment);
    }

    public List<CorporateShipmentResponseDTO> getShipmentsForStoreOwner(Long storeOwnerId) {
        return shipmentRepository.findByOrder_Store_OwnerId(storeOwnerId).stream()
                .map(this::convertToCorporateResponseDTO)
                .collect(Collectors.toList());
    }

    public CorporateShipmentResponseDTO getShipmentById(Long shipmentId, Long storeOwnerId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new EntityNotFoundException("Shipment not found"));

        // SECURITY CHECK: Verify Ownership before getting
        if (!storeOwnerId.equals(shipment.getOrder().getStore().getOwner().getId())) {
            throw new SecurityException("You do not have permission to get this shipment");
        }

        return convertToCorporateResponseDTO(shipment);
    }

    private boolean canAccessShipment(Shipment shipment, Long userId, String role) {
        boolean isOwner = userId != null && userId.equals(shipment.getOrder().getUser().getId());
        boolean isAdmin = "ADMIN".equals(role);
        boolean isStoreOwner = userId != null && userId.equals(shipment.getOrder().getStore().getOwner().getId());

        return isOwner || isAdmin || isStoreOwner;
    }

    private ShipmentResponseDTO convertToResponseDTO(Shipment shipment) {
        ShipmentResponseDTO dto = new ShipmentResponseDTO();
        dto.setId(shipment.getId());
        dto.setTrackingId(shipment.getTrackingId());
        dto.setMode(shipment.getMode());
        dto.setStatus(shipment.getStatus());
        dto.setWarehouse(shipment.getWarehouse());
        dto.setProductImportance(shipment.getProductImportance());

        if (shipment.getOrder() != null) {
            dto.setOrderId(shipment.getOrder().getId());
        }
        return dto;
    }

    private CorporateShipmentResponseDTO convertToCorporateResponseDTO(Shipment shipment) {
        CorporateShipmentResponseDTO dto = new CorporateShipmentResponseDTO();
        dto.setId(shipment.getId());
        dto.setTrackingId(shipment.getTrackingId());
        dto.setMode(shipment.getMode());
        dto.setStatus(shipment.getStatus());
        dto.setWarehouse(shipment.getWarehouse());
        dto.setProductImportance(shipment.getProductImportance());

        if (shipment.getOrder() != null) {
            dto.setOrderId(shipment.getOrder().getId());
            dto.setCustomer(new CustomerSummaryDTO(
                    shipment.getOrder().getUser().getId(),
                    shipment.getOrder().getUser().getName(),
                    shipment.getOrder().getUser().getSurname(),
                    shipment.getOrder().getUser().getEmail()));
        return dto;
    }

    public List<CorporateShipmentResponseDTO> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(this::convertToCorporateResponseDTO)
                .collect(Collectors.toList());
    }
}