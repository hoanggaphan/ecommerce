package com.springboot.ecommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.springboot.ecommerce.dto.ProductDto;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.service.ProductService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;
  private final ModelMapper modelMapper;

  @GetMapping()
  public ResponseEntity<Map<String, Object>> getAllProducts(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "15") int length,
      @RequestParam(required = false) String color,
      @RequestParam(required = false) String size,
      @RequestParam(defaultValue = "") String keyword) {

    Map<String, Object> response = productService.getAllProducts(page, length, color, size, keyword);

    return new ResponseEntity<>(response, HttpStatus.OK);

    // return productService.getAllProducts(page, length, color, size).stream()
    // .map(product -> modelMapper.map(product, ProductDto.class))
    // .collect(Collectors.toList());
  }

  @GetMapping("/{slug}")
  public ProductDto getProduct(@PathVariable("slug") String slug) {
    return modelMapper.map(productService.getProduct(slug), ProductDto.class);
  }

  @GetMapping("/popular")
  public List<ProductDto> getProductByKey() {
    return productService.getPopularProduct().stream()
        .map(product -> modelMapper.map(product, ProductDto.class))
        .collect(Collectors.toList());
  }

  @PostMapping()
  public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
    // convert DTO to entity
    Product productReq = modelMapper.map(productDto, Product.class);

    Product product = productService.createProduct(productReq);

    // convert entity to DTO
    ProductDto productRes = modelMapper.map(product, ProductDto.class);
    return new ResponseEntity<ProductDto>(productRes, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id,
      @Valid @RequestBody ProductDto productDto) {
    // convert DTO to entity
    Product productReq = modelMapper.map(productDto, Product.class);

    Product product = productService.updateProduct(id, productReq);

    // convert entity to DTO
    ProductDto productRes = modelMapper.map(product, ProductDto.class);
    return new ResponseEntity<ProductDto>(productRes, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable("id") Long id) {
    productService.deleteProduct(id);
  }

}
