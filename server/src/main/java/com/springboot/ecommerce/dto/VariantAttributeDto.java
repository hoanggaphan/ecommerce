package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.springboot.ecommerce.model.EmbeddedId.VariantAttributeId;

import lombok.Data;

@Data
public class VariantAttributeDto {
  private VariantAttributeId ids;

  @NotBlank(message = "{value.notBlank}")
  @Size(max = 50, message = "{string.maxSize50}")
  private String value;
}
