package com.challenge.product_api.service;

import com.challenge.product_api.dto.ProductRequestDTO;
import com.challenge.product_api.dto.ProductResponseDTO;
import com.challenge.product_api.mapper.ProductMapper;
import com.challenge.product_api.model.Product;
import com.challenge.product_api.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService service;

    private Product product;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Description")
                .price(10.0)
                .stockQuantity(5)
                .build();

        requestDTO = ProductRequestDTO.builder()
                .name("Test Product")
                .description("Description")
                .price(10.0)
                .stockQuantity(5)
                .build();

        responseDTO = ProductResponseDTO.builder()
                .id(1L)
                .name("Test Product")
                .description("Description")
                .price(10.0)
                .stockQuantity(5)
                .build();
    }

    @Test
    void testCreate() {
        when(productMapper.toEntity(requestDTO)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = service.create(requestDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(repository).save(product);
    }

    @Test
    void testFindAll_NoFilter() {
        when(repository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> results = service.findAll(null, null);

        assertEquals(1, results.size());
        assertEquals("Test Product", results.get(0).getName());
    }

    @Test
    void testFindAll_WithNameFilter() {
        when(repository.findByNameContainingIgnoreCase("Test")).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> results = service.findAll("Test", null);

        assertEquals(1, results.size());
        verify(repository).findByNameContainingIgnoreCase("Test");
    }

    @Test
    void testFindAll_SortAsc() {
        Product p2 = Product.builder().id(2L).name("B").price(5.0).stockQuantity(1).build();
        when(repository.findAll()).thenReturn(Arrays.asList(product, p2));
        when(productMapper.toDTO(any())).thenReturn(responseDTO);

        List<ProductResponseDTO> results = service.findAll(null, "asc");

        assertEquals(2, results.size());
        verify(repository).findAll();
    }

    @Test
    void testFindById_Found() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = service.findById(1L);

        assertEquals("Test Product", result.getName());
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void testUpdate_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO updated = service.update(1L, requestDTO);

        assertEquals("Test Product", updated.getName());
        verify(repository).save(product);
    }

    @Test
    void testUpdate_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(1L, requestDTO));
    }

    @Test
    void testDelete_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(repository).delete(product);

        service.delete(1L);

        verify(repository).delete(product);
    }

    @Test
    void testDelete_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
    }
}
