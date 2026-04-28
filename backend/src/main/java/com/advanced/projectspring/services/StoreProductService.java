package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.corporate.StoreProductsRequestDTO;
import com.advanced.projectspring.dto.corporate.StoreProductsResponseDTO;
import com.advanced.projectspring.models.Category;
import com.advanced.projectspring.models.Product;
import com.advanced.projectspring.models.Store;
import com.advanced.projectspring.repositories.CategoryRepository;
import com.advanced.projectspring.repositories.ProductRepository;
import com.advanced.projectspring.repositories.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private StoreRepository storeRepository;

    // Helper method to get the logged-in user's store securely
    private Store getStoreByUserId(Long userId) {
        List<Store> stores = storeRepository.findByOwnerId(userId);
        if (stores.isEmpty()) {
            throw new EntityNotFoundException("Store not found for this user");
        }
        return stores.get(0); // We assume one store per corporate user
    }

    // Helper method to convert an Entity to a ResponseDTO
    private StoreProductsResponseDTO mapToResponseDTO(Product product) {
        StoreProductsResponseDTO dto = new StoreProductsResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setStock(product.getStock());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        // Calculate low stock dynamically (less than 10 items)
        dto.setLowStock(product.getStock() != null && product.getStock() < 10);
        return dto;
    }

    // 1. READ (List user's products)
    public List<StoreProductsResponseDTO> listProductsByStoreId(Long userId) {
        Store store = getStoreByUserId(userId);
        return productRepository.findByStoreId(store.getId()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<StoreProductsResponseDTO> getProductByIdAndStoreId(Long productId, Long userId) {
        // 1. Get the actual Store entity using the userId
        Store store = getStoreByUserId(userId);

        // 2. Pass the store.getId() to the repository, not the userId!
        return productRepository.findByIdAndStoreId(productId, store.getId())
                .map(this::mapToResponseDTO);
    }

    // 2. CREATE (Add a new product)
    public StoreProductsResponseDTO addProduct(Long userId, StoreProductsRequestDTO request) {
        Store store = getStoreByUserId(userId);

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        Product product = new Product();
        product.setStore(store);
        product.setCategory(category);
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setUnitPrice(request.getUnitPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());

        Product savedProduct = productRepository.save(product);
        return mapToResponseDTO(savedProduct);
    }

    // 3. UPDATE (Edit an existing product)
    public StoreProductsResponseDTO updateProduct(Long userId, Long productId, StoreProductsRequestDTO request) {
        Store store = getStoreByUserId(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Security check: Make sure this corporate user actually owns the product!
        if (!product.getStore().getId().equals(store.getId())) {
            throw new IllegalArgumentException("You do not have permission to edit this product");
        }

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        product.setCategory(category);
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setUnitPrice(request.getUnitPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDTO(updatedProduct);
    }

    // 4. DELETE (Remove a product)
    public void deleteProduct(Long userId, Long productId) {
        Store store = getStoreByUserId(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Security check: Make sure this corporate user actually owns the product!
        if (!product.getStore().getId().equals(store.getId())) {
            throw new IllegalArgumentException("You do not have permission to delete this product");
        }

        productRepository.delete(product);
    }

    public List<StoreProductsResponseDTO> getMyTopReviewedProducts(Long corporateUserId, Pageable pageable) {
        return productRepository.findMostReviewedByStoreOwner(corporateUserId, pageable)
                .stream()
                .map(this::mapToResponseDTO) // Uses your existing corporate mapping logic
                .collect(Collectors.toList());
    }

    public List<StoreProductsResponseDTO> getMyHighestRatedProducts(Long corporateUserId, Pageable pageable) {
        return productRepository.findHighestRatedByStoreOwner(corporateUserId, pageable)
                .stream()
                .map(this::mapToResponseDTO) // Uses your existing corporate mapping logic
                .collect(Collectors.toList());
    }
}
