package com.springboot.ecommerce.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.springboot.ecommerce.dto.AttributeDto;
import com.springboot.ecommerce.model.Attribute;
import com.springboot.ecommerce.service.AttributeService;

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
@RequestMapping("/api/v1/attributes")
@RequiredArgsConstructor
public class AttributeController {
  private final AttributeService attributeService;
  private final ModelMapper modelMapper;

  @GetMapping()
  public List<AttributeDto> getAllAttributes() {
    return attributeService.getAllAttributes().stream()
        .map(attribute -> modelMapper.map(attribute, AttributeDto.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public AttributeDto getAttribute(@PathVariable("id") Long id) {
    return modelMapper.map(attributeService.getAttribute(id), AttributeDto.class);
  }

  @PostMapping()
  public ResponseEntity<AttributeDto> createAttribute(@Valid @RequestBody AttributeDto attributeDto) {
    // convert DTO to entity
    Attribute attributeReq = modelMapper.map(attributeDto, Attribute.class);

    Attribute attribute = attributeService.createAttribute(attributeReq);

    // convert entity to DTO
    AttributeDto attributeRes = modelMapper.map(attribute, AttributeDto.class);
    return new ResponseEntity<AttributeDto>(attributeRes, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AttributeDto> updateAttribute(@PathVariable("id") Long id,
      @Valid @RequestBody AttributeDto attributeDto) {
    // convert DTO to entity
    Attribute attributeReq = modelMapper.map(attributeDto, Attribute.class);

    Attribute attribute = attributeService.updateAttribute(id, attributeReq);

    // convert entity to DTO
    AttributeDto attributeRes = modelMapper.map(attribute, AttributeDto.class);
    return new ResponseEntity<AttributeDto>(attributeRes, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public void deleteAttribute(@PathVariable("id") Long id) {
    attributeService.deleteAttribute(id);
  }
}
