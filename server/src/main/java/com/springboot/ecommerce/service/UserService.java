package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.dto.CartItemsDto;
import com.springboot.ecommerce.model.Role;
import com.springboot.ecommerce.model.User;

public interface UserService {
  public List<User> getAllUsers();

  public User getUser(String username);

  public User createUser(User user);

  public User updateUser(Long id, User user);

  public void deleteUser(Long id);

  public Role createRole(Role role);

  public void addRoleToUser(String username, String roleName);

  public List<CartItemsDto> addItemToCart(String username, Long variantId, String qty);
}
