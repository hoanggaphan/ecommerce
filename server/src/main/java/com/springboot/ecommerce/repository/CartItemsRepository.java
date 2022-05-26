package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.CartItems;
import com.springboot.ecommerce.model.EmbeddedId.CartItemsId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemsRepository extends JpaRepository<CartItems, CartItemsId> {

}
