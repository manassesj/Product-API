package com.challenge.product_api.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidProduct() {
        Product product = Product.builder()
                .name("Valid Product")
                .description("Description")
                .price(10.0)
                .stockQuantity(5)
                .build();

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNameNotBlank() {
        Product product = Product.builder()
                .name("")
                .price(10.0)
                .stockQuantity(5)
                .build();

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Product name is required")));
    }

    @Test
    void testPricePositive() {
        Product product = Product.builder()
                .name("Product")
                .price(-1.0)
                .stockQuantity(5)
                .build();

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price must be greater than zero")));
    }

    @Test
    void testStockQuantityNonNegative() {
        Product product = Product.builder()
                .name("Product")
                .price(10.0)
                .stockQuantity(-1)
                .build();

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Stock quantity must be zero or positive")));
    }

    @Test
    void testPrePersistSetsCreatedAt() {
        Product product = Product.builder()
                .name("Product")
                .price(10.0)
                .stockQuantity(5)
                .build();

        assertNull(product.getCreatedAt());
        product.prePersist();
        assertNotNull(product.getCreatedAt());
        assertTrue(product.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
