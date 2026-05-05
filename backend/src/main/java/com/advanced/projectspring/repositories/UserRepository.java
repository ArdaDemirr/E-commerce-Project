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

    /*
     * used by login, user enters email and password, find user by email and check
     * if password matches
     */
    Optional<User> findByEmail(String email);
    // SELECT * FROM users WHERE email = ?
    // Optional means it might return null if not found — safer than returning null
    // directly

    /*
     * used by admin to get all users with a specific role (corporate or individual)
     */
    List<User> findByRole(String role);
    // SELECT * FROM users WHERE role = ?
    // Returns a list because multiple users can have the same role

    /*
     * used by admin to get all active or suspended users
     */
    List<User> findByActive(boolean active);
    // SELECT * FROM users WHERE active = ?
}
