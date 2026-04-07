package com.advanced.projectspring.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import com.advanced.projectspring.dto.UserResponseDTO;
import com.advanced.projectspring.dto.admin.AdminUserRequestDTO;
import com.advanced.projectspring.models.User;
import com.advanced.projectspring.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
