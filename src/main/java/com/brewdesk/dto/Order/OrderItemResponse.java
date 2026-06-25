package com.brewdesk.dto.order;

import java.math.BigDecimal;

import com.brewdesk.dto.product.ProductResponse;

public record OrderItemResponse(
    Long id,
    ProductResponse product,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal,
    String observation
) {
    
}
