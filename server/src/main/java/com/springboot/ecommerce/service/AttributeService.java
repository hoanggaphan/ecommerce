package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.model.Attribute;

public interface AttributeService {
  public List<Attribute> getAllAttributes();

  public Attribute getAttribute(Long id);

  public Attribute createAttribute(Attribute attribute);

  public Attribute updateAttribute(Long id, Attribute attribute);

  public void deleteAttribute(Long id);
}
