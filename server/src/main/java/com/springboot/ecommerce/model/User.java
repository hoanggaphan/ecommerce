package com.springboot.ecommerce.model;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.ecommerce.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "{username.notBlank}")
  @Size(max = 32, message = "{string.maxSize32}")
  @Column(length = 32, nullable = false, unique = true)
  private String username;

  @NotBlank(message = "{password.notBlank}")
  @Column(nullable = false)
  private String password;

  @Size(max = 100, message = "{string.maxSize100}")
  @Column(name = "first_name", length = 100)
  private String firstName;

  @Size(max = 100, message = "{string.maxSize100}")
  @Column(name = "last_name", length = 100)
  private String lastName;

  @Email(message = "{email.notValid}")
  @Size(max = 100)
  @Column(length = 100)
  private String email;

  @Size(min = 10, max = 10, message = "{phone.size}")
  @Column(length = 10)
  private String phone;

  @Column(name = "birth_day")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDay;

  @Size(max = 100, message = "{string.maxSize100}")
  @Column(length = 100)
  private String address1;

  @Size(max = 100, message = "{string.maxSize100}")
  @Column(length = 100)
  private String address2;

  @Column(length = 10)
  @Enumerated(EnumType.STRING)
  private Gender gender = Gender.male;

  @ManyToMany(fetch = FetchType.EAGER)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Collection<Role> roles;

  // 1 user c?? nhi???u ????n ?????t h??ng
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<Order> orders;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Collection<CartItems> cartItems;
}
