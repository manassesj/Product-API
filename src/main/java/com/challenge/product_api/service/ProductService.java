package com.challenge.product_api.service;

import com.challenge.product_api.dto.ProductRequestDTO;
import com.challenge.product_api.dto.ProductResponseDTO;
import com.challenge.product_api.mapper.ProductMapper;
import com.challenge.product_api.model.Product;
import com.challenge.product_api.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    public ProductResponseDTO create(ProductRequestDTO productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product saved = repository.save(product);
        log.info("Product created successfully: id={}, name={}", saved.getId(), saved.getName());
        return productMapper.toDTO(saved);
    }

    public List<ProductResponseDTO> findAll(String name, String sortByPrice) {
        List<Product> products;
        if (name != null && !name.isEmpty()) {
            products = repository.findByNameContainingIgnoreCase(name);
            log.info("Searching products by name: {}", name);
        } else {
            products = repository.findAll();
        }

        if ("asc".equalsIgnoreCase(sortByPrice)) {
            products.sort(Comparator.comparing(Product::getPrice));
            log.info("Sorting products by price ascending");
        } else if ("desc".equalsIgnoreCase(sortByPrice)) {
            products.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
            log.info("Sorting products by price descending");
        }

        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with id={}", id);
                    return new EntityNotFoundException("Product not found");
                });
        return productMapper.toDTO(product);
    }

    public List<ProductResponseDTO> searchProductsByName(String name) {
        log.info("Searching products with name containing: {}", name);
        return repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO productRequest) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempt to update non-existing product id={}", id);
                    return new EntityNotFoundException("Product not found");
                });

        existing.setName(productRequest.getName());
        existing.setDescription(productRequest.getDescription());
        existing.setPrice(productRequest.getPrice());
        existing.setStockQuantity(productRequest.getStockQuantity());

        Product updated = repository.save(existing);
        log.info("Product updated successfully: id={}", updated.getId());
        return productMapper.toDTO(updated);
    }

    public void delete(Long id) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempt to delete non-existing product id={}", id);
                    return new EntityNotFoundException("Product not found");
                });

        repository.delete(existing);
        log.info("Product deleted successfully: id={}", id);
    }
}
