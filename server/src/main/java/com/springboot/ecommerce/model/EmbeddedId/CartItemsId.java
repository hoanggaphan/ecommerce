package com.springboot.ecommerce.model.EmbeddedId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class CartItemsId implements Serializable {
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "variant_id")
  private Long variantId;
}
