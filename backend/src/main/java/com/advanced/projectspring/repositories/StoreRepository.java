package com.advanced.projectspring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advanced.projectspring.models.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByOwnerId(Long ownerId); // return a list of stores owned by a specific user

    List<Store> findByStatus(String status); // return a list of stores with a specific status
}
