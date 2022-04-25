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
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 100, message = "{string.maxSize100}")
  @Column(length = 100, nullable = false, unique = true)
  private String name;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 200, message = "{string.maxSize100}")
  @Column(length = 200, nullable = false, unique = true)
  private String slug;

  @Size(max = 1000, message = "{string.maxSize1000}")
  @Column(length = 1000)
  private String description;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 2000, message = "{string.maxSize2000}")
  @Column(name = "img_default", length = 2000, nullable = false)
  private String imgDefault;

  @NotNull(message = "{field.notBlank}")
  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean active = false;

  // Nhiều product thuộc 1 category.
  @ManyToOne
  @JoinColumn(name = "category_id", nullable = true)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Category category;

  // 1 product có nhiều dạng loại khác nhau (sku)
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<Variant> variants;

  // 1 product có nhiều image
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<Image> images;
}
