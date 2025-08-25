package com.challenge.product_api.mapper;

import com.challenge.product_api.dto.ProductRequestDTO;
import com.challenge.product_api.dto.ProductResponseDTO;
import com.challenge.product_api.model.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void testToEntity() {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .name("Product 1")
                .description("Description 1")
                .price(100.0)
                .stockQuantity(10)
                .build();

        Product entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("Product 1", entity.getName());
        assertEquals("Description 1", entity.getDescription());
        assertEquals(100.0, entity.getPrice());
        assertEquals(10, entity.getStockQuantity());
    }

    @Test
    void testToEntityWithNull() {
        Product entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testToDTO() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description 1");
        product.setPrice(100.0);
        product.setStockQuantity(10);
        product.setCreatedAt(LocalDateTime.now());

        ProductResponseDTO dto = mapper.toDTO(product);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Product 1", dto.getName());
        assertEquals("Description 1", dto.getDescription());
        assertEquals(100.0, dto.getPrice());
        assertEquals(10, dto.getStockQuantity());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    void testToDTOWithNull() {
        ProductResponseDTO dto = mapper.toDTO(null);
        assertNull(dto);
    }
}
