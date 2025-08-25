package com.challenge.product_api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidProductRequest() {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name("Test Product")
                .description("Description")
                .price(100.0)
                .stockQuantity(10)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidProductName() {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name("")
                .price(50.0)
                .stockQuantity(5)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Product name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testNegativePrice() {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name("Product")
                .price(-10.0)
                .stockQuantity(5)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than zero", violations.iterator().next().getMessage());
    }

    @Test
    void testNegativeStockQuantity() {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name("Product")
                .price(10.0)
                .stockQuantity(-1)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals("Stock quantity must be zero or positive", violations.iterator().next().getMessage());
    }

    @Test
    void testNullOptionalFields() {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name("Product")
                .price(10.0)
                .stockQuantity(0)
                .description(null)
                .build();

        Set<ConstraintViolation<ProductRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
