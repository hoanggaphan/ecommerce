package com.springboot.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.springboot.ecommerce.dto.CartItemsDto;
import com.springboot.ecommerce.exception.MessageInternalException;
import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.CartItems;
import com.springboot.ecommerce.model.Role;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.model.Variant;
import com.springboot.ecommerce.model.EmbeddedId.CartItemsId;
import com.springboot.ecommerce.repository.CartItemsRepository;
import com.springboot.ecommerce.repository.RoleRepository;
import com.springboot.ecommerce.repository.UserRepository;
import com.springboot.ecommerce.service.UserService;
import com.springboot.ecommerce.service.VariantService;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final CartItemsRepository cartItemsRepo;
  private final VariantService variantService;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if (user == null) {
      log.error("User not found in the database");
      throw new UsernameNotFoundException("User not found in the database");
    }

    log.info("User found in the database: {}", username);
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
  }

  public List<User> getAllUsers() {
    log.info("Fetching all users");
    return userRepo.findAll();
  }

  public User getUser(String username) {
    log.info("Fetching user {}", username);
    User user = userRepo.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found in the database");
    }
    return user;
  }

  public User createUser(User user) {
    log.info("Saving new user {} to the database", user.getUsername());
    if (userRepo.existsByUsername(user.getUsername())) {
      throw new ResourceAlreadyExistException("User", "username", user.getUsername());
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    User user = userRepo.findByUsername(username);
    if (user == null)
      throw new ResourceNotFoundException("User", "username", username);
    Role role = roleRepo.findByName(roleName);
    if (role == null)
      throw new ResourceNotFoundException("User", "role name", roleName);
    user.getRoles().add(role);
  }

  public List<CartItemsDto> addItemToCart(String username, Long variantId, String qty) {
    log.info("Adding variant id {} to cart of user {}", variantId, username);

    int newQty;
    try {
      newQty = Integer.parseInt(qty);
    } catch (Exception e) {
      throw new MessageInternalException("qty not valid!");
    }

    if (newQty <= 0)
      throw new MessageInternalException("qty not valid!");

    // Check User in DB
    User user = userRepo.findByUsername(username);
    if (user == null)
      throw new ResourceNotFoundException("User", "username", username);

    // Check Variant in DB
    Variant variant = variantService.getVariant(variantId);

    CartItemsId ids = new CartItemsId();
    ids.setUserId(user.getId());
    ids.setVariantId(variant.getId());
    Optional<CartItems> optItem = cartItemsRepo.findById(ids);

    // Nếu trong giỏ hàng user tồn tại sp thì +1, ngược lại thêm mới id sp vào giỏ.
    if (optItem.isPresent()) {
      CartItems item = optItem.get();
      item.setQty(newQty);
      cartItemsRepo.save(item);
    } else {
      CartItems newItem = new CartItems();
      newItem.setQty(newQty);
      newItem.setUser(user);
      newItem.setVariant(variant);
      user.getCartItems().add(newItem);
    }

    return user.getCartItems().stream()
        .map(cartItem -> modelMapper.map(cartItem, CartItemsDto.class))
        .collect(Collectors.toList());
  }
}
