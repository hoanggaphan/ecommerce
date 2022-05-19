package com.springboot.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.springboot.ecommerce.exception.MessageInternalException;
import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.Attribute;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.Variant;
import com.springboot.ecommerce.model.VariantAttribute;
import com.springboot.ecommerce.repository.AttributeRepository;
import com.springboot.ecommerce.repository.ProductRepository;
import com.springboot.ecommerce.repository.VariantRepository;
import com.springboot.ecommerce.service.VariantService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantServiceImpl implements VariantService {
  private final VariantRepository variantRepository;
  private final ProductRepository productRepository;
  private final AttributeRepository attributeRepository;

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

    if (variant.getOptions() != null) {
      List<VariantAttribute> optionsList = new ArrayList<>();

      for (VariantAttribute option : variant.getOptions()) {
        Optional<Attribute> attrInDB = attributeRepository.findById(option.getIds().getAttributeId());

        if (!attrInDB.isPresent()) {
          throw new MessageInternalException("Id not valid!");
        }

        option.setVariant(variant);
        option.setAttribute(attrInDB.get());
        optionsList.add(option);
      }

      variant.setOptions(optionsList);
    }

    Product productInDB = productRepository.findById(variant.getProduct().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", variant.getProduct().getId()));

    variant.setProduct(productInDB);

    return variantRepository.save(variant);
  }

  public Variant updateVariant(Long id, Variant variant) {
    variantRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Variant", "id", id));

    Variant sku = variantRepository.findBySkuIgnoreCaseAndIdNot(variant.getSku(), id);
    if (sku != null) {
      throw new ResourceAlreadyExistException("Variant", "name", variant.getSku());
    }

    if (variant.getOptions() != null) {
      List<VariantAttribute> optionsList = new ArrayList<>();

      for (VariantAttribute option : variant.getOptions()) {
        Long variantId = option.getIds().getVariantId();
        if (variantId != null && id != variantId) {
          throw new MessageInternalException("Id not valid!");
        }

        Optional<Attribute> optAttr = attributeRepository.findById(option.getIds().getAttributeId());

        if (!optAttr.isPresent()) {
          throw new MessageInternalException("Id not valid!");
        }

        option.setVariant(variant);
        option.setAttribute(optAttr.get());
        optionsList.add(option);
      }

      variant.setOptions(optionsList);
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
