package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserPassDto {
  @NotBlank(message = "{username.notBlank}")
  @Size(max = 32, message = "{string.maxSize32}")
  private String username;

  @NotBlank(message = "{password.notBlank}")
  private String password;
}
