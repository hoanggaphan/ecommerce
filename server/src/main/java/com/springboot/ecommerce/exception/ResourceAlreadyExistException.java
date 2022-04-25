package com.springboot.ecommerce.exception;

public class ResourceAlreadyExistException extends RuntimeException {
  public ResourceAlreadyExistException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
  }
}
