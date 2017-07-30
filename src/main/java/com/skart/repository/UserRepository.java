package com.skart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skart.entity.User;

/**
 * User repository for CRUD operations.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
