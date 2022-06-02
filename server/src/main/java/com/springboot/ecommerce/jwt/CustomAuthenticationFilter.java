package com.springboot.ecommerce.jwt;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.ecommerce.dto.UserPassDto;
import com.springboot.ecommerce.model.CustomUserDetails;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  @Autowired
  private JwtProvider jwtProvider;

  @Override
  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

  // Xác thực đăng nhập
  // Vai trò như controller, service với path là /api/v1/login
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    String username, password;
    try {
      UserPassDto userPass = new ObjectMapper().readValue(request.getInputStream(), UserPassDto.class);
      username = userPass.getUsername();
      password = userPass.getPassword();
    } catch (IOException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
    log.info("Username logging is: {}", username);
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
        password);
    return this.getAuthenticationManager().authenticate(authenticationToken);
  }

  // Đăng nhập thành công
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();
    String access_token = jwtProvider.generateAccessToken(user, request);
    String refresh_token = jwtProvider.generateRefreshToken(user, request);

    log.info("Logging Success");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(new JSONObject()
        .put("access_token", access_token)
        .put("refresh_token", refresh_token)
        .toString());
  }

  // Xử lý đăng nhập ko thành công
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {
    log.error("Logging Failed: {}", failed.getLocalizedMessage());
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(new JSONObject()
        .put("timestamp", LocalDateTime.now())
        .put("status", HttpStatus.UNAUTHORIZED.value())
        .put("error", HttpStatus.UNAUTHORIZED)
        .put("message", "username or password invalid!")
        .toString());
  }
}
