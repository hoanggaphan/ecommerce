package com.springboot.ecommerce.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "{name.notBlank}")
  @Size(max = 50, message = "{string.maxSize50}")
  @Column(length = 50, nullable = false, unique = true)
  private String name;

  @NotBlank(message = "{slug.notBlank}")
  @Size(max = 100, message = "{string.maxSize100}")
  @Column(length = 100, nullable = false, unique = true)
  private String slug;

  @Size(max = 1000, message = "{string.maxSize1000}")
  @Column(length = 1000)
  private String description;

  @NotBlank(message = "{img.notBlank}")
  @Size(max = 2000, message = "{string.maxSize2000}")
  @Column(length = 2000, nullable = false)
  private String img;

  // 1 category có nhiều product)
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<Product> products;
}
