package com.springboot.ecommerce.repository;

import java.util.Optional;

import com.springboot.ecommerce.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);
}
