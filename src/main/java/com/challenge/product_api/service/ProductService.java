package com.challenge.product_api.service;

import com.challenge.product_api.model.Product;
import com.challenge.product_api.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product create(Product product) {
        return repository.save(product);
    }

    public List<Product> findAll(String name, String sortByPrice) {
        List<Product> products;
        if (name != null && !name.isEmpty()) {
            products = repository.findByNameContainingIgnoreCase(name);
        } else {
            products = repository.findAll();
        }

        if ("asc".equalsIgnoreCase(sortByPrice)) {
            products.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
        } else if ("desc".equalsIgnoreCase(sortByPrice)) {
            products.sort((a, b) -> b.getPrice().compareTo(a.getPrice()));
        }

        return products;
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product update(Long id, Product newProduct) {
        Product existing = findById(id);
        existing.setName(newProduct.getName());
        existing.setDescription(newProduct.getDescription());
        existing.setPrice(newProduct.getPrice());
        existing.setStockQuantity(newProduct.getStockQuantity());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Product existing = findById(id);
        repository.delete(existing);
    }
}