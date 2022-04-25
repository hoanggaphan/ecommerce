package com.springboot.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.springboot.ecommerce.dto.ProductDto;
import com.springboot.ecommerce.exception.ResourceAlreadyExistException;
import com.springboot.ecommerce.exception.ResourceNotFoundException;
import com.springboot.ecommerce.model.Image;
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
    Optional<Product> optionalProduct = productRepository.findBySlug(slug);

    if (optionalProduct.isPresent()) {
      return optionalProduct.get();
    } else {
      throw new ResourceNotFoundException("Product", "slug", slug);
    }

  }

  public Product createProduct(Product product) {
    Optional<Product> optionalProductSlug = productRepository.findBySlug(product.getSlug());

    if (optionalProductSlug.isPresent()) {
      throw new ResourceAlreadyExistException("Product", "slug", product.getSlug());
    }

    Optional<Product> optionalProductName = productRepository.findByName(product.getName());

    if (optionalProductName.isPresent()) {
      throw new ResourceAlreadyExistException("Product", "name", product.getName());
    }

    List<Image> images = new ArrayList<>();
    for (Image imgItem : product.getImages()) {
      Image image = new Image();
      image.setUrl(imgItem.getUrl());
      image.setProduct(product);
      images.add(image);
    }
    product.setImages(images);

    return productRepository.save(product);
  }

  public Product updateProduct(Long id, Product product) {
    Product productInDB = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    product.setId(id);

    Optional<Product> optProduct = Optional.of(productInDB);

    if (optProduct.get().getSlug().equals(product.getSlug())) {
      throw new ResourceAlreadyExistException("Product", "slug",
          product.getSlug());
    }

    if (optProduct.get().getName().equals(product.getName())) {
      throw new ResourceAlreadyExistException("Product", "name",
          product.getName());
    }

    // Update images
    List<Image> images = new ArrayList<>();
    for (Image imgItem : product.getImages()) {
      imgItem.setProduct(product);
      images.add(imgItem);
    }
    product.setImages(images);

    return productRepository.save(product);
  }

  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}
