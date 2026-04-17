package com.advanced.projectspring.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.advanced.projectspring.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.util.List;

import com.advanced.projectspring.dto.UserResponseDTO;
import com.advanced.projectspring.dto.admin.AdminUserRequestDTO;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;

    // GET ALL OF THEM
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET SINGLE ONE WITH ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // UPDATE ROLE
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateRolePatch(@PathVariable Long id,
            @Valid @RequestBody AdminUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateRole(id, request));
    }

    // UPDATE ROLE
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateRole(@PathVariable Long id,
            @Valid @RequestBody AdminUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateRole(id, request));
    }

    // UPDATE ACTIVE
    @PutMapping("/{id}/active")
    public ResponseEntity<UserResponseDTO> updateActive(@PathVariable Long id,
            @Valid @RequestBody AdminUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateActive(id, request));
    }

    // UPDATE ACTIVE
    @PatchMapping("/{id}/active")
    public ResponseEntity<UserResponseDTO> updateActivePatch(@PathVariable Long id,
            @Valid @RequestBody AdminUserRequestDTO request) {
        return ResponseEntity.ok(userService.updateActive(id, request));
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}