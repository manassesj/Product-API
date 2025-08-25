package com.challenge.product_api.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductResponseDTOTest {

    @Test
    void testGettersAndSetters() {
        ProductResponseDTO dto = new ProductResponseDTO();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(1L);
        dto.setName("Test Product");
        dto.setDescription("Description");
        dto.setPrice(100.0);
        dto.setStockQuantity(10);
        dto.setCreatedAt(now);

        assertEquals(1L, dto.getId());
        assertEquals("Test Product", dto.getName());
        assertEquals("Description", dto.getDescription());
        assertEquals(100.0, dto.getPrice());
        assertEquals(10, dto.getStockQuantity());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ProductResponseDTO dto = ProductResponseDTO.builder()
                .id(2L)
                .name("Another Product")
                .description("Another Description")
                .price(50.0)
                .stockQuantity(5)
                .createdAt(now)
                .build();

        assertEquals(2L, dto.getId());
        assertEquals("Another Product", dto.getName());
        assertEquals("Another Description", dto.getDescription());
        assertEquals(50.0, dto.getPrice());
        assertEquals(5, dto.getStockQuantity());
        assertEquals(now, dto.getCreatedAt());
    }
}
