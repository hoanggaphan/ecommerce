package com.springboot.ecommerce.service;

import java.util.List;
import java.util.Map;

import com.springboot.ecommerce.model.Product;

public interface ProductService {
  public Map<String, Object> getAllProducts(int page, int length, String color, String size, String keyword);

  public List<Product> getPopularProduct();

  public Product getProduct(String slug);

  public Product createProduct(Product product);

  public Product updateProduct(Long id, Product product);

  public void deleteProduct(Long id);
}
