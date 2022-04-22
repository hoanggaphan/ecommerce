package com.springboot.ecommerce.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  // Handle error in server and all error still not catch
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> globalExceptionHandling(Exception e, WebRequest request) {
    List<String> details = new ArrayList<>();

    // Not show for client
    details.add(e.getLocalizedMessage());

    ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error",
        HttpStatus.INTERNAL_SERVER_ERROR.value(), details,
        ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Handle when url not found
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> resourceNotFoundHandling(ResourceNotFoundException e, WebRequest request) {
    List<String> details = new ArrayList<>();
    details.add(e.getLocalizedMessage());

    ErrorDetails errorDetails = new ErrorDetails(HttpStatus.NOT_FOUND, "Resource Not Found",
        HttpStatus.NOT_FOUND.value(), details,
        ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  // Handle for method POST and PUT, validate arguments in body
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
      HttpStatus status, WebRequest request) {

    List<String> details = new ArrayList<>();

    for (ObjectError error : e.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }

    ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation Failed",
        HttpStatus.BAD_REQUEST.value(), details,
        ZonedDateTime.now(ZoneId.of("Z")));
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }
}
