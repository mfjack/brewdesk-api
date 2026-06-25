package com.brewdesk.dto.product;

import java.math.BigDecimal;

import com.brewdesk.dto.category.CategoryResponse;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    CategoryResponse category
) {
    
}
