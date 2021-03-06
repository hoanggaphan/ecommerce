package com.springboot.ecommerce.config;

import com.springboot.ecommerce.jwt.CustomAuthenticationFilter;
import com.springboot.ecommerce.jwt.CustomAuthorizationFilter;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  // Bỏ trang error trong mvc để có thể return error dạng json khi có lỗi 403
  @Bean
  static BeanFactoryPostProcessor removeErrorSecurityFilter() {
    return (beanFactory) -> ((DefaultListableBeanFactory) beanFactory)
        .removeBeanDefinition("errorPageSecurityInterceptor");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public CustomAuthenticationFilter customAuthenticationFilter()
      throws Exception {
    return new CustomAuthenticationFilter();
  }

  @Bean
  public CustomAuthorizationFilter customAuthorizationFilter() {
    return new CustomAuthorizationFilter();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CustomAuthenticationFilter authenticationFilter = customAuthenticationFilter();
    authenticationFilter.setAuthenticationManager(authenticationManagerBean());
    authenticationFilter.setFilterProcessesUrl("/api/login");

    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**").permitAll();

    http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority("ROLE_USER",
        "ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/users/**").hasAnyAuthority("ROLE_USER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAnyAuthority("ROLE_USER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyAuthority("ROLE_ADMIN",
        "ROLE_SUPER_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/categories/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN",
        "ROLE_SUPER_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN",
        "ROLE_SUPER_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/attributes/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/attributes/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/attributes/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN",
        "ROLE_SUPER_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/variants/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/variants/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/variants/**").hasAnyAuthority("ROLE_MANAGER",
        "ROLE_ADMIN",
        "ROLE_SUPER_ADMIN");

    http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyAuthority("ROLE_USER",
        "ROLE_MANAGER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyAuthority("ROLE_USER");
    http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasAnyAuthority("ROLE_USER",
        "ROLE_SUPER_ADMIN");
    http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasAnyAuthority("ROLE_SUPER_ADMIN");

    http.authorizeRequests().anyRequest().authenticated();
    http.addFilter(authenticationFilter);
    http.addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  // Những api public ko cần phân quyền (chỉ với method GET)
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers(HttpMethod.GET, "/api/v1/categories/**")
        .antMatchers(HttpMethod.GET, "/api/v1/products/**")
        .antMatchers(HttpMethod.GET, "/api/v1/variants/**")
        .antMatchers(HttpMethod.GET, "/api/v1/attributes/**");
  }
}
