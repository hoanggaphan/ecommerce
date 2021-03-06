package com.springboot.ecommerce.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.springboot.ecommerce.dto.CartItemsDto;
import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.CartItems;
import com.springboot.ecommerce.model.CustomUserDetails;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
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
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if (user == null) {
      log.error("User not found in the database");
      throw new UsernameNotFoundException("User not found in the database");
    }

    log.info("User found in the database: {}", username);
    return new CustomUserDetails(user);
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

  @Transactional
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

  @Transactional
  public List<CartItemsDto> addCart(String username, List<CartItemsDto> CartItemsDto) {
    log.info("Adding cart for user {}", username);

    // Check User in DB
    User user = userRepo.findByUsername(username);
    if (user == null)
      throw new ResourceNotFoundException("User", "username", username);

    List<CartItems> cartItems = CartItemsDto.stream().map(cartItem -> {
      return modelMapper.map(cartItem, CartItems.class);
    }).collect(Collectors.toList());

    // Check item valid in cart
    cartItems.stream().forEach(
        (cartItem) -> {
          // Check Variant in DB
          Variant variant = variantService.getVariant(cartItem.getVariant().getId());
          cartItem.setUser(user);
          cartItem.setVariant(variant);
        });

    user.getCartItems().clear();
    userRepo.flush();
    user.getCartItems().addAll(cartItems);

    // Tr??? v??? gi??? h??ng m???i c???a user
    return user.getCartItems().stream()
        .map(cartItem -> modelMapper.map(cartItem, CartItemsDto.class))
        .collect(Collectors.toList());
  }

  @Transactional
  public List<CartItemsDto> updateCart(String username, Long variantId, Integer qty) {
    log.info("Updating cart of user {}", username);

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

    // N???u trong gi??? h??ng t???n t???i sp th?? c???p nh???t s??? l?????ng, ng?????c l???i th??m m???i sp
    // v??o gi???.
    if (optItem.isPresent()) {
      CartItems item = optItem.get();
      item.setQty(qty);
      cartItemsRepo.save(item);
    } else {
      CartItems newItem = new CartItems();
      newItem.setQty(qty);
      newItem.setUser(user);
      newItem.setVariant(variant);
      user.getCartItems().add(newItem);
    }

    // Tr??? v??? gi??? h??ng m???i c???a user
    return user.getCartItems().stream()
        .map(cartItem -> modelMapper.map(cartItem, CartItemsDto.class))
        .collect(Collectors.toList());
  }

  @Transactional
  public List<CartItemsDto> removeItemFromCart(String username, Long variantId) {
    log.info("Removing variant id {} from cart of user {}", variantId, username);
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

    // Lo???i b??? sp t??m dc kh???i gi??? h??ng.
    user.getCartItems().remove(optItem.get());

    // Tr??? v??? gi??? h??ng m???i c???a user
    return user.getCartItems().stream()
        .map(cartItem -> modelMapper.map(cartItem, CartItemsDto.class))
        .collect(Collectors.toList());
  }
}
