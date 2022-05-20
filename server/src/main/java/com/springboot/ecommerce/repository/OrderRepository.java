package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
