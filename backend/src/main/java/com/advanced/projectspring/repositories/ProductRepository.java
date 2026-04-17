package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Retrieves a paginated list of products ordered by the total number of
     * reviews.
     * Uses a LEFT JOIN to ensure products with zero reviews are still included in
     * the result set,
     * and relies on the database layer to aggregate the count for optimal
     * performance.
     *
     * @param pageable Defines the limit (e.g., top 5 or 10 results) to prevent
     *                 loading the entire table.
     * @return A list of Product entities sorted by review count in descending
     *         order.
     */
    @Query("SELECT p FROM Product p LEFT JOIN Review r ON r.product = p GROUP BY p.id ORDER BY COUNT(r.id) DESC")
    List<Product> findMostReviewedProducts(Pageable pageable);

    /**
     * Retrieves a paginated list of products ordered by their average star rating.
     * By calculating the AVG() at the database level, it avoids pulling all reviews
     * into
     * application memory, ensuring $O(1)$ memory complexity on the Spring Boot
     * server.
     *
     * @param pageable Defines the limit for the result set.
     * @return A list of Product entities sorted by average rating in descending
     *         order.
     */
    @Query("SELECT p FROM Product p LEFT JOIN Review r ON r.product = p GROUP BY p.id ORDER BY AVG(r.starRating) DESC")
    List<Product> findHighestRatedProducts(Pageable pageable);

    // Corrected: Uses .owner.id to match your Store.java model
    @Query("SELECT p FROM Product p LEFT JOIN Review r ON r.product = p WHERE p.store.owner.id = :userId GROUP BY p.id ORDER BY COUNT(r.id) DESC")
    List<Product> findMostReviewedByStoreOwner(@Param("userId") Long userId, Pageable pageable);

    // Corrected: Uses .owner.id to match your Store.java model
    @Query("SELECT p FROM Product p LEFT JOIN Review r ON r.product = p WHERE p.store.owner.id = :userId GROUP BY p.id ORDER BY AVG(r.starRating) DESC")
    List<Product> findHighestRatedByStoreOwner(@Param("userId") Long userId, Pageable pageable);
}
