package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoryDto {
  private Long id;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 50, message = "{string.maxSize50}")
  private String name;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 100, message = "{string.maxSize100}")
  private String slug;

  @Size(max = 1000, message = "{string.maxSize1000}")
  private String description;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 2000, message = "{string.maxSize2000}")
  private String img;
}
