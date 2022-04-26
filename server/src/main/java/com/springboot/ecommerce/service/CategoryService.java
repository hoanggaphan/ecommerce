package com.springboot.ecommerce.service;

import java.util.Collection;
import java.util.List;

import com.springboot.ecommerce.model.Category;
import com.springboot.ecommerce.model.Product;

public interface CategoryService {
  public List<Category> getAllCategories();

  public Category getCategory(Long id);

  public Collection<Product> getCategoryProducts(String slug);

  public Category createCategory(Category category);

  public Category updateCategory(Long id, Category category);

  public void deleteCategory(Long id);
}
