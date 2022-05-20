package com.springboot.ecommerce.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.springboot.ecommerce.dto.OrderDto;
import com.springboot.ecommerce.model.Order;
import com.springboot.ecommerce.service.OrderService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;
  private final ModelMapper modelMapper;

  @GetMapping()
  public List<OrderDto> getAllOrders() {
    return orderService.getAllOrders().stream()
        .map(order -> modelMapper.map(order, OrderDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public OrderDto getOrder(@PathVariable("id") Long id) {
    return modelMapper.map(orderService.getOrder(id), OrderDto.class);
  }

  @PostMapping()
  public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
    // convert DTO to entity
    Order req = modelMapper.map(orderDto, Order.class);

    Order order = orderService.createOrder(req);

    // convert entity to DTO
    OrderDto res = modelMapper.map(order, OrderDto.class);
    return new ResponseEntity<OrderDto>(res, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long id,
      @Valid @RequestBody OrderDto orderDto) {
    // convert DTO to entity
    Order req = modelMapper.map(orderDto, Order.class);

    Order order = orderService.updateOrder(id, req);

    // convert entity to DTO
    OrderDto res = modelMapper.map(order, OrderDto.class);
    return new ResponseEntity<OrderDto>(res, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public void deleteOrder(@PathVariable("id") Long id) {
    orderService.deleteOrder(id);
  }
}
