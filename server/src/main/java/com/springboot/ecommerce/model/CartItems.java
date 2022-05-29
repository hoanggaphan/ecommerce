package com.springboot.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.springboot.ecommerce.model.EmbeddedId.CartItemsId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart_items")
public class CartItems {
  @EmbeddedId
  private CartItemsId ids = new CartItemsId();

  @NotNull(message = "{qty.notBlank}")
  @Positive(message = "{qty.positive}")
  @Column(nullable = false)
  private Integer qty;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @MapsId("variantId")
  @JoinColumn(name = "variant_id")
  private Variant variant;
}
