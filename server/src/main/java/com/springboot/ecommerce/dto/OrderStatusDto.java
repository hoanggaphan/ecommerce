package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotNull;

import com.springboot.ecommerce.enums.Status;

import lombok.Data;

@Data
public class OrderStatusDto {
  private Long id;
  @NotNull(message = "{status.notBlank}")
  private Status status;
}
