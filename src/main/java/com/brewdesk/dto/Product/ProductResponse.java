package com.brewdesk.dto.Product;

import java.math.BigDecimal;

import com.brewdesk.dto.Category.CategoryResponse;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    CategoryResponse category
) {
    
}
