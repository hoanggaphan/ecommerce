package com.springboot.ecommerce.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springboot.ecommerce.dto.UserDto;
import com.springboot.ecommerce.dto.UserPassDto;
import com.springboot.ecommerce.model.Role;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.service.UserService;

import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final ModelMapper modelMapper;

  @GetMapping("/v1/users")
  public List<UserDto> getAllUsers() {
    return userService.getAllUsers()
        .stream()
        .map(user -> modelMapper.map(user, UserDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/v1/users/{username}")
  public UserDto getUser(@PathVariable("username") String username) {
    return modelMapper.map(userService.getUser(username), UserDto.class);
  }

  // Register
  @PostMapping("/v1/users")
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserPassDto userPassDto) {
    // convert DTO to entity
    User userReq = modelMapper.map(userPassDto, User.class);

    User user = userService.createUser(userReq);

    // convert entity to DTO
    UserDto userRes = modelMapper.map(user, UserDto.class);
    return new ResponseEntity<UserDto>(userRes, HttpStatus.CREATED);
  }

  @PutMapping("/v1/users/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) {
    // convert DTO to entity
    User userReq = modelMapper.map(userDto, User.class);

    User user = userService.updateUser(id, userReq);

    // convert entity to DTO
    UserDto userRes = modelMapper.map(user, UserDto.class);

    return new ResponseEntity<UserDto>(userRes, HttpStatus.OK);
  }

  @DeleteMapping("/v1/users/{id}")
  public void deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
  }

  @PostMapping("/v1/roles")
  public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
    Role res = userService.createRole(role);
    return new ResponseEntity<Role>(res, HttpStatus.CREATED);
  }

  @PostMapping("/v1/roles/add-to-user")
  public ResponseEntity<?> addRoleToUser(@Valid @RequestBody RoleToUserForm form) {
    userService.addRoleToUser(form.getUsername(), form.getRoleName());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/token/refresh")
  public void refreshTOken(HttpServletRequest request,
      HttpServletResponse response) throws JSONException, IOException {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      try {
        String refresh_token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String username = decodedJWT.getSubject();
        User user = userService.getUser(username);

        String access_token = JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 phút
            .withIssuer(request.getRequestURI().toString())
            .withClaim("roles",
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
            .sign(algorithm);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject()
            .put("access_token", access_token)
            .put("refresh_token", refresh_token)
            .toString());
      } catch (Exception e) {
        // Xử lý token không hợp lệ hoặc hét hạn
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject()
            .put("timestamp", LocalDateTime.now())
            .put("status", HttpStatus.FORBIDDEN.value())
            .put("error", HttpStatus.FORBIDDEN)
            .put("message", e.getMessage())
            .toString());
      }
    } else {
      throw new RuntimeException("Refresh token is missing");
    }
  }
}

@Data
class RoleToUserForm {
  private String username;
  private String roleName;
}
