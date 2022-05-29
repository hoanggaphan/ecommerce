package com.springboot.ecommerce.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "{sku.notBlank}")
  @Size(max = 100, message = "{string.maxSize100}")
  @Column(length = 100, nullable = false, unique = true)
  private String sku;

  @NotNull(message = "{originalPrice.notBlank}")
  @Column(name = "original_price", columnDefinition = "double default 0", nullable = false)
  private double originalPrice = 0;

  @NotNull(message = "{qty.notBlank}")
  @Column(columnDefinition = "integer default 0", nullable = false)
  private Integer qty = 0;

  @NotNull(message = "{isDefault.notBlank}")
  @Column(name = "is_default", columnDefinition = "boolean default false", nullable = false)
  private Boolean isDefault = false;

  // Nhiều dạng thuộc 1 product.
  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false) // thông qua khóa ngoại product_id
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Product product;

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<VariantAttribute> options;

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<OrderItems> orderItems;

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<CartItems> cartItems;
}
