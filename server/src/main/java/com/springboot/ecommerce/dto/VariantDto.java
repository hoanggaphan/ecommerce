package com.springboot.ecommerce.dto;

import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class VariantDto {
  private Long id;

  @NotBlank(message = "{sku.notBlank}")
  @Size(max = 100, message = "{string.maxSize100}")
  private String sku;

  @NotNull(message = "{originalPrice.notBlank}")
  private double originalPrice;

  @NotNull(message = "{qty.notBlank}")
  private int qty;

  @NotNull(message = "{isDefault.notBlank}")
  private Boolean isDefault;

  @JsonIgnoreProperties("variants")
  private ProductDto product;

  private Collection<VariantAttributeDto> options;
}