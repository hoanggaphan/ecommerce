package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.springboot.ecommerce.domain.Role;

import lombok.Data;

@Data
public class UserPassDto {
  @NotBlank(message = "{field.notBlank}")
  @Size(max = 32, message = "{string.maxSize32}")
  private String account;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 32, message = "{string.maxSize32}")
  private String password;

  private Role role;
}
