package com.advanced.projectspring.controllers;

import com.advanced.projectspring.dto.corporate.StoreProductsRequestDTO;
import com.advanced.projectspring.dto.corporate.StoreProductsResponseDTO;
import com.advanced.projectspring.services.StoreProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/corporate/products")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class StoreProductController {

    @Autowired
    private StoreProductService storeProductService;

    // userId is extracted from the JWT cookie by JwtAuthFilter and set as a request
    // attribute.
    // No need to manually parse the Authorization header anymore.
    private Long getUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw new org.springframework.security.access.AccessDeniedException("User ID not found in token");
        }
        return (Long) userId;
    }

    @GetMapping
    public ResponseEntity<List<StoreProductsResponseDTO>> getAllProducts(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(storeProductService.listProductsByStoreId(userId));
    }

    @PostMapping
    public ResponseEntity<StoreProductsResponseDTO> addProduct(
            HttpServletRequest request,
            @RequestBody StoreProductsRequestDTO productRequestDTO) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(storeProductService.addProduct(userId, productRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreProductsResponseDTO> updateProduct(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody StoreProductsRequestDTO productRequestDTO) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(storeProductService.updateProduct(userId, id, productRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            HttpServletRequest request,
            @PathVariable Long id) {
        Long userId = getUserId(request);
        storeProductService.deleteProduct(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analytics/most-reviewed")
    public ResponseEntity<List<StoreProductsResponseDTO>> getMyTopReviewed(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Long userId = (Long) request.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(storeProductService.getMyTopReviewedProducts(userId, pageable));
    }

    @GetMapping("/analytics/highest-rated")
    public ResponseEntity<List<StoreProductsResponseDTO>> getMyHighestRated(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Long userId = (Long) request.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(storeProductService.getMyHighestRatedProducts(userId, pageable));
    }
}
