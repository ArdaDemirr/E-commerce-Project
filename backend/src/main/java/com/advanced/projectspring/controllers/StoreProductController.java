package com.advanced.projectspring.controllers;

import com.advanced.projectspring.dto.corporate.StoreProductsRequestDTO;
import com.advanced.projectspring.dto.corporate.StoreProductsResponseDTO;
import com.advanced.projectspring.services.StoreProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/corporate/products")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PreAuthorize("hasRole('CORPORATE')")
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

    // GET ALL PRODUCTS
    @GetMapping
    public ResponseEntity<List<StoreProductsResponseDTO>> getAllProducts(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(storeProductService.listProductsByStoreId(userId));
    }

    // GET SINGLE PRODUCT WITH ID
    @GetMapping("/{id}")
    public ResponseEntity<StoreProductsResponseDTO> getProductById(
            HttpServletRequest request,
            @PathVariable Long id) {

        Long userId = getUserId(request);

        // SECURITY FIX: The service must now verify the product belongs to this userId
        return storeProductService.getProductByIdAndStoreId(id, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE PRODUCT
    @PostMapping
    public ResponseEntity<StoreProductsResponseDTO> addProduct(
            HttpServletRequest request,
            @Valid @RequestBody StoreProductsRequestDTO productRequestDTO) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(storeProductService.addProduct(userId, productRequestDTO));
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    public ResponseEntity<StoreProductsResponseDTO> updateProduct(
            HttpServletRequest request,
            @PathVariable Long id,
            @Valid @RequestBody StoreProductsRequestDTO productRequestDTO) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(storeProductService.updateProduct(userId, id, productRequestDTO));
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            HttpServletRequest request,
            @PathVariable Long id) {
        Long userId = getUserId(request);
        storeProductService.deleteProduct(userId, id);
        return ResponseEntity.noContent().build();
    }

    // GET MOST REVIEWED PRODUCTS BY STORE
    @GetMapping("/analytics/most-reviewed")
    public ResponseEntity<List<StoreProductsResponseDTO>> getMyTopReviewed(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Long userId = (Long) request.getAttribute("userId");
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(storeProductService.getMyTopReviewedProducts(userId, pageable));
    }

    // GET HIGHEST RATED PRODUCTS BY STORE
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
