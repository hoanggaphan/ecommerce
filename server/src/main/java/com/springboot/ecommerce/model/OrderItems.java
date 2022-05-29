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

import com.springboot.ecommerce.model.EmbeddedId.OrderItemsId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItems {
  @EmbeddedId
  private OrderItemsId ids = new OrderItemsId();

  @ManyToOne
  @MapsId("orderId")
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @MapsId("variantId")
  @JoinColumn(name = "variant_id")
  private Variant variant;

  @NotNull(message = "{qty.notBlank}")
  @Positive(message = "{qty.positive}")
  @Column(nullable = false)
  private Integer qty;

  @NotNull(message = "{orderedPrice.notBlank}")
  @Column(name = "ordered_price", nullable = false)
  private double orderedPrice;
}
