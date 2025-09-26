package com.product_management_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.product_management_service.dto.ProductRequestDTO;
import com.product_management_service.dto.ProductResponseDTO;
import com.product_management_service.model.Product;
import com.product_management_service.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
private final ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(convertToResponseDTO(product));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO requestDTO) {
        Product savedProduct = productService.save(requestDTO);
        return new ResponseEntity<>(convertToResponseDTO(savedProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO requestDTO) {
        Product updatedProduct = productService.update(id, requestDTO);
        return ResponseEntity.ok(convertToResponseDTO(updatedProduct));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Metode helper untuk konversi dari Entity ke DTO Response
    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }}