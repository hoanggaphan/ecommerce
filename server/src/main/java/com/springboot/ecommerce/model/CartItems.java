package com.springboot.ecommerce.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
  private CartItemsId ids;

  @NotNull(message = "field.notBlank")
  @Column(nullable = false)
  private int qty;

  @ManyToOne(cascade = CascadeType.ALL)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(cascade = CascadeType.ALL)
  @MapsId("variantId")
  @JoinColumn(name = "variant_id")
  private Variant variant;
}