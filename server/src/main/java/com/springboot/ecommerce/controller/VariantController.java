package com.springboot.ecommerce.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.springboot.ecommerce.dto.VariantDto;
import com.springboot.ecommerce.model.Variant;
import com.springboot.ecommerce.service.VariantService;

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
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class VariantController {
  private final VariantService VariantService;
  private final ModelMapper modelMapper;

  @GetMapping()
  public List<VariantDto> getAllVariants() {
    return VariantService.getAllVariants().stream()
        .map(variant -> modelMapper.map(variant, VariantDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public VariantDto getVariant(@PathVariable("id") Long id) {
    return modelMapper.map(VariantService.getVariant(id), VariantDto.class);
  }

  @PostMapping()
  public ResponseEntity<VariantDto> createVariant(@Valid @RequestBody VariantDto variantDto) {
    // convert DTO to entity
    Variant variantReq = modelMapper.map(variantDto, Variant.class);

    Variant variant = VariantService.createVariant(variantReq);

    // convert entity to DTO
    VariantDto variantRes = modelMapper.map(variant, VariantDto.class);
    return new ResponseEntity<VariantDto>(variantRes, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<VariantDto> updateVariant(@PathVariable("id") Long id,
      @Valid @RequestBody VariantDto variantDto) {
    // convert DTO to entity
    Variant variantReq = modelMapper.map(variantDto, Variant.class);

    Variant variant = VariantService.updateVariant(id, variantReq);

    // convert entity to DTO
    VariantDto variantRes = modelMapper.map(variant, VariantDto.class);
    return new ResponseEntity<VariantDto>(variantRes, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public void deleteVariant(@PathVariable("id") Long id) {
    VariantService.deleteVariant(id);
  }
}
