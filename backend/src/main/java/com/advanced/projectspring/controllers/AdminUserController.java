package com.advanced.projectspring.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.advanced.projectspring.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

import com.advanced.projectspring.dto.UserResponseDTO;
import com.advanced.projectspring.dto.admin.AdminUserRequestDTO;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // PatchMapping for role — frontend sends PATCH /api/admin/users/{id}/role
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateRolePatch(@PathVariable Long id,
            @RequestBody AdminUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateRole(id, request));
    }

    // PutMapping kept for backward compatibility
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateRole(@PathVariable Long id,
            @RequestBody AdminUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateRole(id, request));
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<UserResponseDTO> updateActive(@PathVariable Long id,
            @RequestBody AdminUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateActive(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

