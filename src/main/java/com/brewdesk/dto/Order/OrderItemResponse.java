package com.brewdesk.dto.Order;

import java.math.BigDecimal;

import com.brewdesk.dto.Product.ProductResponse;

public record OrderItemResponse(
    Long id,
    ProductResponse product,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subtotal,
    String observation
) {
    
}
