package com.challenge.product_api.mapper;

import com.challenge.product_api.dto.ProductRequestDTO;
import com.challenge.product_api.dto.ProductResponseDTO;
import com.challenge.product_api.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    public ProductResponseDTO toDTO(Product entity) {
        if (entity == null) {
            return null;
        }
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
