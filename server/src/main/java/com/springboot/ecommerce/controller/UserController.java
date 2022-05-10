package com.springboot.ecommerce.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.springboot.ecommerce.dto.UserDto;
import com.springboot.ecommerce.dto.UserPassDto;
import com.springboot.ecommerce.model.Role;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.service.UserService;

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

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final ModelMapper modelMapper;

  @GetMapping("/users")
  public List<UserDto> getAllUsers() {
    return userService.getAllUsers()
        .stream()
        .map(user -> modelMapper.map(user, UserDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/users/{username}")
  public UserDto getUser(@PathVariable("username") String username) {
    return modelMapper.map(userService.getUser(username), UserDto.class);
  }

  // Register
  @PostMapping("/users")
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserPassDto userPassDto) {
    // convert DTO to entity
    User userReq = modelMapper.map(userPassDto, User.class);

    User user = userService.createUser(userReq);

    // convert entity to DTO
    UserDto userRes = modelMapper.map(user, UserDto.class);
    return new ResponseEntity<UserDto>(userRes, HttpStatus.CREATED);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
    // convert DTO to entity
    User userReq = modelMapper.map(userDto, User.class);

    User user = userService.updateUser(id, userReq);

    // convert entity to DTO
    UserDto userRes = modelMapper.map(user, UserDto.class);

    return new ResponseEntity<UserDto>(userRes, HttpStatus.OK);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
  }

  @PostMapping("/roles")
  public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
    Role res = userService.createRole(role);
    return new ResponseEntity<Role>(res, HttpStatus.CREATED);
  }

  @PostMapping("/roles/add-to-user")
  public ResponseEntity<?> addRoleToUser(@Valid @RequestBody RoleToUserForm form) {
    userService.addRoleToUser(form.getUsername(), form.getRoleName());
    return ResponseEntity.ok().build();
  }
}

@Data
class RoleToUserForm {
  private String username;
  private String roleName;
}
