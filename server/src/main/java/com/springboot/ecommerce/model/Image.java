package com.springboot.ecommerce.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
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
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "{url.notBlank}")
  @Size(max = 2000, message = "{string.maxSize2000}")
  @Column(length = 2000, nullable = false)
  private String url;

  // Nhiều Image thuộc 1 product.
  @ManyToOne(cascade = {CascadeType.MERGE})
  @JoinColumn(name = "product_id", nullable = false)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Product product;
}
