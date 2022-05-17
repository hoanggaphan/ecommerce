package com.springboot.ecommerce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.springboot.ecommerce.dto.ProductDto;
import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.repository.ProductRepository;
import com.springboot.ecommerce.service.ProductService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final ModelMapper modelMapper;

  public Map<String, Object> getAllProducts(int page, int length, String color, String size, String keyword) {
    if (page == 0) {
      throw new ResourceNotFoundException("Product", "page", page);
    }

    Pageable paging = PageRequest.of(--page, length);

    Page<Product> pageProduct;

    pageProduct = productRepository.findByNameContainingIgnoreCase(keyword, paging);

    // Get list and convert to DTO
    List<ProductDto> products = pageProduct.getContent().stream()
        .map(product -> modelMapper.map(product, ProductDto.class))
        .collect(Collectors.toList());

    Map<String, Object> response = new HashMap<>();

    response.put("products", products);
    response.put("currentPage", pageProduct.getNumber() + 1);
    response.put("totalItems", pageProduct.getTotalElements());
    response.put("totalPages", pageProduct.getTotalPages());

    return response;
    // Page<Product> pageProducts;

    // if (!color.isBlank()) {
    // return productRepository.findByColorContainingIgnoreCase(color, paging);
    // }

  }

  // Dùng để demo
  public List<Product> getPopularProduct() {
    Page<Product> page = productRepository.findAll(PageRequest.of(0, 9));
    return page.toList();
  }

  public Product getProduct(String slug) {
    Product product = productRepository.findBySlugIgnoreCase(slug);

    if (product == null) {
      throw new ResourceNotFoundException("Product", "slug", slug);
    }

    return product;
  }

  public Product createProduct(Product product) {
    Product productSlug = productRepository.findBySlugIgnoreCase(product.getSlug());

    if (productSlug != null) {
      throw new ResourceAlreadyExistException("Product", "slug", product.getSlug());
    }

    Product productName = productRepository.findByNameIgnoreCase(product.getName());
    if (productName != null) {
      throw new ResourceAlreadyExistException("Product", "name", product.getName());
    }

    return productRepository.save(product);
  }

  public Product updateProduct(String slug, Product product) {
    // Use Route Param to check product in db
    Product productInDB = productRepository.findBySlugIgnoreCase(slug);
    if (productInDB == null) {
      throw new ResourceNotFoundException("Product", "slug", slug);
    }

    // Check field slug is already existing in db
    Product productSlug = productRepository.findBySlugIgnoreCaseAndSlugNot(product.getSlug(), slug);
    if (productSlug != null) {
      throw new ResourceAlreadyExistException("Product", "slug", product.getSlug());
    }

    // Check field name is already existing in db
    Product productName = productRepository.findByNameIgnoreCaseAndSlugNot(product.getName(), slug);
    if (productName != null) {
      throw new ResourceAlreadyExistException("Product", "name", product.getName());
    }

    product.setId(productInDB.getId());
    return productRepository.save(product);
  }

  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}
