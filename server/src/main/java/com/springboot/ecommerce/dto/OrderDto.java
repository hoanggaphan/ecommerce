package com.springboot.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OrderDto {
  private Long id;

  @NotNull(message = "{shipCost.notBlank}")
  private double shipCost;

  @NotNull(message = "{totalPrice.notBlank}")
  private double totalPrice;

  @NotNull(message = "{orderDateTime.notBlank}")
  private LocalDateTime orderDateTime;

  private Long userId;

  private Collection<OrderItemsDto> orderItems;
}
