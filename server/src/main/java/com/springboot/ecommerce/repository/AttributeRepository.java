package com.springboot.ecommerce.repository;

import java.util.Optional;

import com.springboot.ecommerce.model.Attribute;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
  Optional<Attribute> findByNameIgnoreCase(String name);

  Optional<Attribute> findByNameIgnoreCaseAndIdNot(String name, Long id);
}
