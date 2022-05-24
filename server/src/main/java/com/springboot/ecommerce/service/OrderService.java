package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.dto.OrderStatusDto;
import com.springboot.ecommerce.model.Order;

public interface OrderService {
  public List<Order> getAllOrders();

  public Order getOrder(Long id);

  public Order createOrder(Order order);

  public Order updateStatus(Long id, Order order, OrderStatusDto status);

  public void deleteOrder(Long id);
}
