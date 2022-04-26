package com.springboot.ecommerce.dto;

import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springboot.ecommerce.model.VariantAttribute;

import lombok.Data;

@Data
public class VariantDto {
  private Long id;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 100, message = "{string.maxSize100}")
  private String sku;

  @NotNull(message = "{field.notBlank}")
  private double originalPrice;

  @NotNull(message = "{field.notBlank}")
  private int qty;

  @NotNull(message = "{field.notBlank}")
  private Boolean isDefault;

  @JsonIgnoreProperties("variants")
  private ProductDto product;

  private Collection<VariantAttribute> options;
}