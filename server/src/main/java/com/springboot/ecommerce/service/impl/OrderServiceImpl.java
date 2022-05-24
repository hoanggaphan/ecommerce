package com.springboot.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.springboot.ecommerce.dto.OrderStatusDto;
import com.springboot.ecommerce.enums.Status;
import com.springboot.ecommerce.exception.MessageInternalException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.Order;
import com.springboot.ecommerce.model.OrderItems;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.model.Variant;
import com.springboot.ecommerce.repository.OrderRepository;
import com.springboot.ecommerce.repository.UserRepository;
import com.springboot.ecommerce.repository.VariantRepository;
import com.springboot.ecommerce.service.OrderService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final VariantRepository variantRepository;
  private final UserRepository userRepository;

  public List<Order> getAllOrders() {
    log.info("Fetching all orders");
    return orderRepository.findAll();

  }

  public Order getOrder(Long id) {
    log.info("Fetching order with id: {}", id);
    return orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order", "Id", id));
  }

  public Order createOrder(Order order) {
    log.info("Saving new order to the database");

    double totalPrice = order.getShipCost();

    User userInDB = userRepository.findById(order.getUser().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Order", "userId", order.getUser().getId()));
    order.setUser(userInDB);
    order.setStatus(Status.pending);

    if (order.getOrderItems() != null) {
      List<OrderItems> orderItems = new ArrayList<>();

      for (OrderItems orderItem : order.getOrderItems()) {
        Optional<Variant> variantInDB = variantRepository.findById(orderItem.getVariant().getId());

        if (!variantInDB.isPresent()) {
          throw new MessageInternalException("Id not valid!");
        }

        // Tính toán số tiền đặt hàng dựa vào thông tin trong DB
        totalPrice += (variantInDB.get().getOriginalPrice() * orderItem.getQty());
        orderItem.setOrderedPrice(variantInDB.get().getOriginalPrice());

        orderItem.setVariant(variantInDB.get());
        orderItem.setOrder(order);
        orderItems.add(orderItem);
      }

      order.setTotalPrice(totalPrice);
      order.setOrderItems(orderItems);
    }

    return orderRepository.save(order);
  }

  public Order updateStatus(Long id, Order order, OrderStatusDto status) {
    log.info("Updating status of order with id: {}", id);
    order.setId(id);
    order.setStatus(status.getStatus());
    return orderRepository.save(order);
  }

  public void deleteOrder(Long id) {
    log.info("Deleting order with id: {}", id);
    orderRepository.deleteById(id);
  }
}
