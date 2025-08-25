package com.challenge.product_api.controller;

import com.challenge.product_api.dto.ProductRequestDTO;
import com.challenge.product_api.dto.ProductResponseDTO;
import com.challenge.product_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO dto) {
        log.info("Creating new product with name='{}'", dto.getName());
        ProductResponseDTO saved = service.create(dto);
        log.info("Product created successfully with id={}", saved.getId());
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortByPrice
    ) {
        log.info("Fetching all products with filters -> name='{}', sortByPrice='{}'", name, sortByPrice);
        List<ProductResponseDTO> list = service.findAll(name, sortByPrice);
        log.info("Fetched {} products", list.size());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        log.info("Fetching product with id={}", id);
        ProductResponseDTO product = service.findById(id);
        log.info("Product found -> id={}, name='{}'", product.getId(), product.getName());
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO dto
    ) {
        log.info("Updating product id={} with new values: name='{}', price={}", id, dto.getName(), dto.getPrice());
        ProductResponseDTO updated = service.update(id, dto);
        log.info("Product updated successfully -> id={}, name='{}'", updated.getId(), updated.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting product with id={}", id);
        service.delete(id);
        log.info("Product with id={} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
