package com.advanced.projectspring.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.User;

@Repository
// which table this manages = User
// type of primary key = long
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    // SELECT * FROM users WHERE email = ?
    // Optional means it might return null if not found — safer than returning null
    // directly

    List<User> findByRole(String role);
    // SELECT * FROM users WHERE role = ?
    // Returns a list because multiple users can have the same role

    List<User> findByActive(boolean active);
    // SELECT * FROM users WHERE active = ?
    // Used by admin to get all active or suspended users
}
