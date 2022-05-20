package com.springboot.ecommerce.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.Category;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.repository.CategoryRepository;
import com.springboot.ecommerce.service.CategoryService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository categoryRepository;

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public Category getCategory(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category", "Id", id));
  }

  public Collection<Product> getCategoryBySlug(String slug) {
    Optional<Category> optionalCategory = categoryRepository.findBySlug(slug);

    if (optionalCategory.isPresent()) {
      return optionalCategory.get().getProducts();
    } else {
      throw new ResourceNotFoundException("Category", "slug", slug);
    }
  }

  public Category createCategory(Category category) {
    Optional<Category> optCategorySlug = categoryRepository.findBySlugIgnoreCase(category.getSlug());

    if (optCategorySlug.isPresent()) {
      throw new ResourceAlreadyExistException("Category", "slug", category.getSlug());
    }

    Optional<Category> optCategoryName = categoryRepository.findByNameIgnoreCase(category.getName());

    if (optCategoryName.isPresent()) {
      throw new ResourceAlreadyExistException("Category", "name", category.getName());
    }

    return categoryRepository.save(category);
  }

  public Category updateCategory(Long id, Category category) {
    categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

    Optional<Category> optCategorySlug = categoryRepository.findBySlugIgnoreCaseAndIdNot(category.getSlug(),
        id);

    if (optCategorySlug.isPresent()) {
      throw new ResourceAlreadyExistException("Category", "slug", category.getSlug());
    }

    Optional<Category> optCategoryName = categoryRepository.findByNameIgnoreCaseAndIdNot(category.getName(), id);

    if (optCategoryName.isPresent()) {
      throw new ResourceAlreadyExistException("Category", "name", category.getName());
    }

    category.setId(id);

    return categoryRepository.save(category);
  }

  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }
}
