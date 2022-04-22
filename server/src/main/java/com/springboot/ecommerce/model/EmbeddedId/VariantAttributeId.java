package com.springboot.ecommerce.model.EmbeddedId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class VariantAttributeId implements Serializable {
  @Column(name = "variant_id")
  private Long variantId;

  @Column(name = "attribute_id")
  private Long attributeId;
}
