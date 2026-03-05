package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStoreId(Long storeId); // return a list of products in a specific store

    List<Product> findByCategoryId(Long categoryId); // return a list of products in a specific category

    List<Product> findByNameContainingIgnoreCase(String name); // return a list of products with a name containing a
                                                               // specific string

    List<Product> findByStoreIdAndCategoryId(Long storeId, Long categoryId); // return a list of products in a specific
                                                                             // store and category
}
