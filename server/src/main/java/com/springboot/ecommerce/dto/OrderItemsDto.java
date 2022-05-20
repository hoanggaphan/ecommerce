package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OrderItemsDto {
  private Long variantId;
  @NotNull(message = "qty.notBlank")
  private int qty;
  @NotNull(message = "orderedPrice.notBlank")
  private double orderedPrice;
}
