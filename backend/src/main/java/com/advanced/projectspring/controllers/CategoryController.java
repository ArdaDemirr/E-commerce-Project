/*
 *exposes endpoint for categories
*/

package com.advanced.projectspring.controllers;

import com.advanced.projectspring.models.Category;
import com.advanced.projectspring.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository; // connect to database

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() { // returns a list of Category objects
        return ResponseEntity.ok(categoryRepository.findAll()); // return all of them
        // GET /api/categories → returns all 23 categories
    }
}