package com.springboot.ecommerce.repository;

import java.util.Optional;

import com.springboot.ecommerce.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findBySlug(String slug);

  Optional<Category> findBySlugIgnoreCase(String slug);

  Optional<Category> findByNameIgnoreCase(String name);

  Optional<Category> findBySlugIgnoreCaseAndIdNot(String slug, Long id);

  Optional<Category> findByNameIgnoreCaseAndIdNot(String name, Long id);
}
