package com.challenge.product_api.controller;

import com.challenge.product_api.dto.ProductRequestDTO;
import com.challenge.product_api.dto.ProductResponseDTO;
import com.challenge.product_api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setName("Test Product");
        requestDTO.setDescription("Description");
        requestDTO.setPrice(100.0);
        requestDTO.setStockQuantity(10);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Product");
        responseDTO.setDescription("Description");
        responseDTO.setPrice(100.0);
        responseDTO.setStockQuantity(10);

        when(productService.create(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<ProductResponseDTO> response = productController.create(requestDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO.getName(), response.getBody().getName());
        verify(productService, times(1)).create(requestDTO);
    }

    @Test
    void testFindAllProducts() {
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1L);
        product1.setName("Product 1");

        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2L);
        product2.setName("Product 2");

        List<ProductResponseDTO> products = Arrays.asList(product1, product2);

        when(productService.findAll(null, null)).thenReturn(products);

        ResponseEntity<List<ProductResponseDTO>> response = productController.findAll(null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(productService, times(1)).findAll(null, null);
    }

    @Test
    void testFindById() {
        ProductResponseDTO product = new ProductResponseDTO();
        product.setId(1L);
        product.setName("Product 1");

        when(productService.findById(1L)).thenReturn(product);

        ResponseEntity<ProductResponseDTO> response = productController.findById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product 1", response.getBody().getName());
        verify(productService, times(1)).findById(1L);
    }

    @Test
    void testUpdateProduct() {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setName("Updated Product");
        requestDTO.setDescription("Updated Description");
        requestDTO.setPrice(200.0);
        requestDTO.setStockQuantity(20);

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Updated Product");
        responseDTO.setDescription("Updated Description");
        responseDTO.setPrice(200.0);
        responseDTO.setStockQuantity(20);

        when(productService.update(1L, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<ProductResponseDTO> response = productController.update(1L, requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Product", response.getBody().getName());
        verify(productService, times(1)).update(1L, requestDTO);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).delete(1L);

        ResponseEntity<Void> response = productController.delete(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(productService, times(1)).delete(1L);
    }
}
