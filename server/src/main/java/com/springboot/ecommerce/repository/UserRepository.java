package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByAccount(String account);
}
