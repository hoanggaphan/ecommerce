package com.springboot.ecommerce.repository;

import java.util.Optional;

import com.springboot.ecommerce.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findBySlugIgnoreCase(String slug);

  Optional<Product> findByNameIgnoreCase(String name);

  Optional<Product> findBySlugIgnoreCaseAndIdNot(String slug, Long id);

  Optional<Product> findByNameIgnoreCaseAndIdNot(String name, Long id);

  Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

  // Page<Product> findBySizeContainingIgnoreCase(String size, Pageable pageable);
}
