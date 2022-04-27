package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.model.User;

public interface UserService {
  public List<User> getAllUsers();

  public User getUser(Long id);

  public User createUser(User user);

  public User updateUser(Long id, User user);

  public void deleteUser(Long id);
}
