package com.advanced.projectspring.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import com.advanced.projectspring.dto.UserResponseDTO;
import com.advanced.projectspring.dto.admin.AdminUserRequestDTO;
import com.advanced.projectspring.models.User;
import com.advanced.projectspring.repositories.UserRepository;
import com.advanced.projectspring.repositories.StoreRepository;
import com.advanced.projectspring.models.Store;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UserResponseDTO mapToAdminUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setGender(user.getGender());
        dto.setActive(user.isActive());
        return dto;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToAdminUserResponseDTO).collect(Collectors.toList());
    }

    public UserResponseDTO updateRole(Long id, AdminUserRequestDTO request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(request.getRole());
        User updatedUser = userRepository.save(user);
        return mapToAdminUserResponseDTO(updatedUser);
    }

    public UserResponseDTO updateActive(Long id, AdminUserRequestDTO request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(request.isActive());
        User updatedUser = userRepository.save(user);
        return mapToAdminUserResponseDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        
        // --- 1. Clean up individual records associated with the user ---
        // Delete reviews written by the user
        jdbcTemplate.update("DELETE FROM reviews WHERE user_id = ?", id);
        
        // Delete order_items from orders placed by the user
        jdbcTemplate.update("DELETE FROM order_items WHERE order_id IN (SELECT id FROM orders WHERE user_id = ?)", id);
        
        // Delete orders placed by the user
        jdbcTemplate.update("DELETE FROM orders WHERE user_id = ?", id);

        // --- 2. If CORPORATE, clean up their store and all its associations ---
        if ("CORPORATE".equalsIgnoreCase(user.getRole())) {
            // Find the store ID(s)
            String storeIdsQuery = "SELECT id FROM stores WHERE owner_id = " + id;
            
            // Delete reviews on products belonging to the store
            jdbcTemplate.update("DELETE FROM reviews WHERE product_id IN (SELECT id FROM products WHERE store_id IN (" + storeIdsQuery + "))");
            
            // Delete order_items linked to products belonging to the store
            jdbcTemplate.update("DELETE FROM order_items WHERE product_id IN (SELECT id FROM products WHERE store_id IN (" + storeIdsQuery + "))");
            
            // Delete order_items of orders placed TO the store
            jdbcTemplate.update("DELETE FROM order_items WHERE order_id IN (SELECT id FROM orders WHERE store_id IN (" + storeIdsQuery + "))");
            
            // Delete orders placed TO the store
            jdbcTemplate.update("DELETE FROM orders WHERE store_id IN (" + storeIdsQuery + ")");
            
            // Delete products belonging to the store
            jdbcTemplate.update("DELETE FROM products WHERE store_id IN (" + storeIdsQuery + ")");
            
            // Finally delete the store(s) itself
            jdbcTemplate.update("DELETE FROM stores WHERE owner_id = ?", id);
        }
        
        // --- 3. Now the user row is free of ALL constraints and can be safely deleted ---
        userRepository.delete(user);
    }
}
