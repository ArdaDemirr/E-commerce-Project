package com.advanced.projectspring.services;

import com.advanced.projectspring.dto.CategoryRequestDTO;
import com.advanced.projectspring.dto.CategoryResponseDTO;
import com.advanced.projectspring.models.Category;
import com.advanced.projectspring.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /*
     * public List<Category> getAllCategories() {
     * return categoryRepository.findAll();
     * }
     */

    // ---------------- GENERAL METHOD TO LIST CATEGORIES ----------------

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    // ---------------- ADMIN METHODS ----------------

    private CategoryResponseDTO mapToCategoryResponseDTO(Category category) {
        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setId(category.getId());
        responseDTO.setCategoryName(category.getName());
        responseDTO.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        responseDTO.setParentCategoryName(category.getParent() != null ? category.getParent().getName() : null);
        return responseDTO;
    }

    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {
        Category category = new Category();
        category.setName(request.getCategoryName());

        if (request.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponseDTO(savedCategory);
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getCategoryName());

        if (request.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(category);
        return mapToCategoryResponseDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}