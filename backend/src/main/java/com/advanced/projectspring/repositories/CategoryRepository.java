package com.advanced.projectspring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /*
     * used when adding products, we look up category by name to get its ID
     * Also used by individual users filtering products by category
     */
    Optional<Category> findByName(String name);
}
