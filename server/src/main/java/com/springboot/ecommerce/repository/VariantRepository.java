package com.springboot.ecommerce.repository;

import java.util.Optional;

import com.springboot.ecommerce.model.Variant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, Long> {
  Optional<Variant> findBySkuIgnoreCase(String slug);

  Variant findBySkuIgnoreCaseAndIdNot(String sku, Long id);
}
