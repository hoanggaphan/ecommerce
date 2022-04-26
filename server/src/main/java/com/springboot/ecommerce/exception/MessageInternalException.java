package com.springboot.ecommerce.exception;

public class MessageInternalException extends RuntimeException {
  public MessageInternalException(String message) {
    super(message);
  }
}
