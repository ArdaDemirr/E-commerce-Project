package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.CategorySummaryDTO;
import com.advanced.projectspring.dto.ProductResponseDTO;
import com.advanced.projectspring.dto.StoreSummaryDTO;
import com.advanced.projectspring.models.Product;
import com.advanced.projectspring.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository; // connect to database

    // Converts Product entity → ProductResponseDTO
    // we use dto to only return thing needed toı be return, not sensitive data
    // and also less data to return, more speed
    private ProductResponseDTO toDTO(Product p) {
        CategorySummaryDTO category = new CategorySummaryDTO(
                p.getCategory().getId(),
                p.getCategory().getName());
        StoreSummaryDTO store = new StoreSummaryDTO(
                p.getStore().getId(),
                p.getStore().getName(),
                p.getStore().getStatus());
        return new ProductResponseDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getUnitPrice(),
                p.getStock(),
                p.getSku(),
                category,
                store);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        // returns all 200 products from database
    }
    // 1. Get all raw products
    // 2. Start a loop/stream
    // 3. Pass EVERY product through the toDTO method
    // 4. Pack them back into a List

    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id).map(this::toDTO);
        // returns single product or empty if not found
    }

    public List<ProductResponseDTO> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream().map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream().map(this::toDTO).collect(Collectors.toList());
        // filter by category
    }

    public List<ProductResponseDTO> getProductsByStore(Long storeId) {
        return productRepository.findByStoreId(storeId).stream().map(this::toDTO).collect(Collectors.toList());
        // filter by store
    }

    public List<ProductResponseDTO> getProductsByStoreAndCategory(Long storeId, Long categoryId) {
        return productRepository.findByStoreIdAndCategoryId(storeId, categoryId).stream().map(this::toDTO)
                .collect(Collectors.toList());
        // filter by store and category
    }
}
