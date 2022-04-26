package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.model.Variant;

public interface VariantService {
  public List<Variant> getAllVariants();

  public Variant getVariant(Long id);

  public Variant createVariant(Variant variant);

  public Variant updateVariant(Long id, Variant variant);

  public void deleteVariant(Long id);
}
