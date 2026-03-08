package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // corporate user sees only THEIR store's products, not other stores
    List<Product> findByStoreId(Long storeId); // return a list of products in a specific store

    // individual user filters products by category (Electronics, Clothing etc.)
    List<Product> findByCategoryId(Long categoryId); // return a list of products in a specific category

    // search bar. User types "headphone" and finds "Wireless Headphones"
    List<Product> findByNameContainingIgnoreCase(String name); // return a list of products with a name containing a
                                                               // specific string

    // corporate filters their own products by category
    List<Product> findByStoreIdAndCategoryId(Long storeId, Long categoryId); // return a list of products in a specific
                                                                             // store and category

    List<Product> findByStoreOwnerId(Long ownerId);
}
