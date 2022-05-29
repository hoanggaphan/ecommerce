package com.springboot.ecommerce.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class CartItemsDto {
  private Long variantId;
  @NotNull(message = "{qty.notBlank}")
  @Positive(message = "{qty.positive}")
  private Integer qty;
}
