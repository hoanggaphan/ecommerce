package com.springboot.ecommerce.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
  @Autowired
  private JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
      throws ServletException, IOException, IllegalArgumentException {
    if (req.getServletPath().equals("/api/login")
        || req.getServletPath().equals("/api/token/refresh")) {
      filterChain.doFilter(req, res);
    } else {
      String authorizationHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        try {
          String token = authorizationHeader.substring("Bearer ".length());
          DecodedJWT decodedJWT = jwtProvider.decodedJWT(token);
          String username = decodedJWT.getSubject();
          List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
          roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
          });
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
              null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          filterChain.doFilter(req, res);
        } catch (Exception e) {
          // X??? l?? token kh??ng h???p l??? ho???c h??t h???n
          log.error("Invalid token: {}", e.getMessage());
          res.setStatus(HttpStatus.FORBIDDEN.value());
          res.setContentType(MediaType.APPLICATION_JSON_VALUE);
          res.setCharacterEncoding("UTF-8");
          res.getWriter().write(new JSONObject()
              .put("timestamp", LocalDateTime.now())
              .put("status", HttpStatus.FORBIDDEN.value())
              .put("error", HttpStatus.FORBIDDEN)
              .put("message", e.getMessage())
              .toString());
        }
      } else {
        filterChain.doFilter(req, res);
      }
    }

  }
}
