package com.challenge.product_api.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be greater than zero")
    private Double price;

    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private Integer stockQuantity;
}