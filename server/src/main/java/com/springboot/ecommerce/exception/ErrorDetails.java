package com.springboot.ecommerce.exception;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {
  private HttpStatus httpStatus;
  private String message;
  private int status;
  private List<String> details;
  private ZonedDateTime timestamp;
}
