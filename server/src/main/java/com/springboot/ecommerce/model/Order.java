package com.springboot.ecommerce.model;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.ecommerce.enums.Status;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"Order\"") // order là keyword trong sql nên phải sử dụng 2 dòng này ms cho tạo bảng Order
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;

  @NotNull(message = "{shipCost.notBlank}")
  @Column(name = "ship_cost", nullable = false)
  private double shipCost;

  @NotNull(message = "{totalPrice.notBlank}")
  @Column(name = "total_price", nullable = false)
  private double totalPrice;

  @NotNull(message = "{orderDateTime.notBlank}")
  @Column(name = "order_date", nullable = false)
  @DateTimeFormat(iso = ISO.DATE_TIME)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime orderDateTime;

  @NotNull(message = "{status.notBlank}")
  @Column(columnDefinition = "varchar(20) default 'pending'", nullable = false)
  private Status status = Status.pending;

  // Nhiều Order thuộc 1 User.
  @ManyToOne
  @JoinColumn(name = "user_id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<OrderItems> orderItems;
}
