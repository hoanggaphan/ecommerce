package com.springboot.ecommerce.exception;

public class ImageNullException extends RuntimeException {
  public ImageNullException() {
    super("Hình ảnh không được null!");
  }
}
