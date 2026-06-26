package com.brewdesk.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.brewdesk.enums.OrderStatus;

public record OrderResponse(
    Long id,
    String customerName,
    OrderStatus status,
    LocalDateTime createdAt,
    BigDecimal total,
    List<OrderItemResponse> orderItems
) {
    
}
