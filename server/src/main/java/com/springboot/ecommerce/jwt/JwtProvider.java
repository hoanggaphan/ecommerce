package com.springboot.ecommerce.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springboot.ecommerce.model.CustomUserDetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:env.properties")
public class JwtProvider {
  @Value("${jwt.secret}")
  private String JwtSecret;
  private final long JWT_EXPIRATION = 60 * 60 * 1000; // 1 tiếng

  public String generateAccessToken(CustomUserDetails userDetail, HttpServletRequest request) {
    Date expiryDate = new Date(System.currentTimeMillis() + JWT_EXPIRATION);
    Algorithm algorithm = Algorithm.HMAC256(JwtSecret.getBytes());
    return JWT.create()
        .withSubject(userDetail.getUsername())
        .withExpiresAt(expiryDate)
        .withIssuer(request.getRequestURI().toString())
        .withClaim("roles",
            userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .sign(algorithm);
  }

  public String generateRefreshToken(CustomUserDetails userDetail, HttpServletRequest request) {
    Date expiryDate = new Date(System.currentTimeMillis() + JWT_EXPIRATION);
    Algorithm algorithm = Algorithm.HMAC256(JwtSecret.getBytes());
    return JWT.create()
        .withSubject(userDetail.getUsername())
        .withExpiresAt(expiryDate)
        .withIssuer(request.getRequestURI().toString())
        .sign(algorithm);
  }

  public DecodedJWT decodedJWT(String token) {
    Algorithm algorithm = Algorithm.HMAC256(JwtSecret.getBytes());
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT;
  }
}
