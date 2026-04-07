/*
 * exposes endpoint for categories
 */

package com.advanced.projectspring.controllers;

import com.advanced.projectspring.dto.CategoryResponseDTO;
import com.advanced.projectspring.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() { // returns a list of Category objects
        return ResponseEntity.ok(categoryService.getAllCategories()); // return all of them
        // GET /api/categories → returns all 23 categories
    }
}