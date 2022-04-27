package com.springboot.ecommerce.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.springboot.ecommerce.domain.Gender;

import lombok.Data;

@Data
public class UserDto {
  private Long id;

  @NotBlank(message = "{field.notBlank}")
  @Size(max = 32, message = "{string.maxSize32}")
  private String account;

  @Size(max = 100, message = "{string.maxSize100}")
  private String firstName;

  @Size(max = 100, message = "{string.maxSize100}")
  private String lastName;

  @Email(message = "{email.notValid}")
  @Size(max = 100)
  private String email;

  @Size(min = 10, max = 10, message = "{phone.size}")
  private String phone;

  private LocalDate birthDay;

  @Size(max = 100, message = "{string.maxSize100}")
  private String address1;

  @Size(max = 100, message = "{string.maxSize100}")
  private String address2;

  private Gender gender;
}
