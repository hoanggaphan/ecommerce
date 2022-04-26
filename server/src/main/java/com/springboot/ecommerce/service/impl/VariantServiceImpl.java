package com.springboot.ecommerce.service.impl;

import java.util.List;
import java.util.Optional;

import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.Variant;
import com.springboot.ecommerce.repository.ProductRepository;
import com.springboot.ecommerce.repository.VariantRepository;
import com.springboot.ecommerce.service.VariantService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {
  private final VariantRepository variantRepository;
  private final ProductRepository productRepository;

  public List<Variant> getAllVariants() {
    return variantRepository.findAll();
  }

  public Variant getVariant(Long id) {
    return variantRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Variant", "Id", id));
  }

  public Variant createVariant(Variant variant) {
    Optional<Variant> optVariantSku = variantRepository.findBySkuIgnoreCase(variant.getSku());

    if (optVariantSku.isPresent()) {
      throw new ResourceAlreadyExistException("Variant", "name", variant.getSku());
    }

    Product productInDB = productRepository.findById(variant.getProduct().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", variant.getProduct().getId()));

    variant.setProduct(productInDB);

    return variantRepository.save(variant);
  }

  public Variant updateVariant(Long id, Variant variant) {
    variantRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Variant", "id", id));

    Optional<Variant> optVariantSku = variantRepository.findBySkuIgnoreCaseAndIdNot(variant.getSku(), id);

    if (optVariantSku.isPresent()) {
      throw new ResourceAlreadyExistException("Variant", "name", variant.getSku());
    }

    Product productInDB = productRepository.findById(variant.getProduct().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", variant.getProduct().getId()));

    variant.setProduct(productInDB);

    variant.setId(id);
    return variantRepository.save(variant);
  }

  public void deleteVariant(Long id) {
    variantRepository.deleteById(id);
  }
}
