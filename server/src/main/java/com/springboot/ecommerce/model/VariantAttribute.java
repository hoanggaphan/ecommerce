package com.springboot.ecommerce.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.springboot.ecommerce.model.EmbeddedId.VariantAttributeId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "variant_attribute")
public class VariantAttribute {
  @EmbeddedId
  private VariantAttributeId ids;

  @ManyToOne(cascade = CascadeType.ALL)
  @MapsId("variantId")
  @JoinColumn(name = "variant_id")
  private Variant variant;

  @ManyToOne(cascade = CascadeType.ALL)
  @MapsId("attributeId")
  @JoinColumn(name = "attribute_id")
  private Attribute attribute;

  @NotBlank(message = "value.notBlank")
  @Size(max = 50, message = "string.maxSize50")
  @Column(length = 50, nullable = false)
  private String value;
}
