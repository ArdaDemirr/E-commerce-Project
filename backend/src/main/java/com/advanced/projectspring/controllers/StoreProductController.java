package com.advanced.projectspring.controllers;

import com.advanced.projectspring.auth.JwtUtil;
import com.advanced.projectspring.dto.corporate.StoreProductsRequestDTO;
import com.advanced.projectspring.dto.corporate.StoreProductsResponseDTO;
import com.advanced.projectspring.services.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/corporate/products")
@CrossOrigin(origins = "http://localhost:4200")
public class StoreProductController { 
    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private JwtUtil jwtUtil; // We need this to get the User ID from the Token!

    @GetMapping
    public ResponseEntity<List<StoreProductsResponseDTO>> getAllProducts(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = jwtUtil.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(storeProductService.listProductsByStoreId(userId));
    }

    @PostMapping
    public ResponseEntity<StoreProductsResponseDTO> addProduct(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody StoreProductsRequestDTO productRequestDTO) {

        Long userId = jwtUtil.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(storeProductService.addProduct(userId, productRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreProductsResponseDTO> updateProduct(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody StoreProductsRequestDTO productRequestDTO) {

        Long userId = jwtUtil.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(storeProductService.updateProduct(userId, id, productRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        Long userId = jwtUtil.extractUserId(authHeader.substring(7));
        storeProductService.deleteProduct(userId, id);
        return ResponseEntity.noContent().build(); // Standard for DELETE is 204 No Content
    }
}
