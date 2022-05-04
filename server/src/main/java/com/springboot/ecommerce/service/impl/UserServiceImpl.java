package com.springboot.ecommerce.service.impl;

import java.util.List;

import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.Role;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.repository.RoleRepository;
import com.springboot.ecommerce.repository.UserRepository;
import com.springboot.ecommerce.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepo;
  private final RoleRepository roleRepo;

  public List<User> getAllUsers() {
    log.info("Fetching all users");

    return userRepo.findAll();
  }

  public User getUser(String username) {
    log.info("Fetching user {}", username);

    return userRepo.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
  }

  public User createUser(User user) {
    log.info("Saving new user {} to the database", user.getUsername());

    if (userRepo.existsByUsername(user.getUsername())) {
      throw new ResourceAlreadyExistException("User", "account", user.getUsername());
    }

    return userRepo.save(user);
  }

  public User updateUser(Long id, User user) {
    log.info("Updating user {}", user.getUsername());

    User userInDB = userRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    user.setId(id);
    user.setPassword(userInDB.getPassword());
    return userRepo.save(user);
  }

  public void deleteUser(Long id) {
    log.info("Deleting user with id: {}", id);

    userRepo.deleteById(id);
  }

  public Role createRole(Role role) {
    log.info("Saving new role {} to the database", role.getName());

    return roleRepo.save(role);
  }

  public void addRoleToUser(String username, String roleName) {
    log.info("Adding role {} to user {}", roleName, username);

    User user = userRepo.findByUsername(username).get();
    Role role = roleRepo.findByName(roleName);
    user.getRoles().add(role);
  }
}
