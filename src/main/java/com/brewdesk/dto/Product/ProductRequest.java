package com.brewdesk.dto.Product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
    @NotBlank(message = "Product name is required")
    String name,

    @NotBlank(message = "Product description is required")
    String description,

    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be a positive value")
    BigDecimal price,

    @NotNull(message = "Product category is required")
    Long categoryId
) { 
}
