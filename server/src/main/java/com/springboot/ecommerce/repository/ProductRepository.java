package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Product findBySlugIgnoreCase(String slug);

  Product findByNameIgnoreCase(String name);

  Product findBySlugIgnoreCaseAndSlugNot(String fieldValue, String slug);

  Product findByNameIgnoreCaseAndSlugNot(String fieldValue, String name);

  Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

  // Page<Product> findBySizeContainingIgnoreCase(String size, Pageable pageable);
}
