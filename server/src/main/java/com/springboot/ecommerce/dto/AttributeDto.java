package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class AttributeDto {
  private Long id;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 50, message = "{string.maxSize50}")
  private String name;
}
