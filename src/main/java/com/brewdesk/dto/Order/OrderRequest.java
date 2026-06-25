package com.brewdesk.dto.Order;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotBlank(message  = "Customer name is required")
    String customerName,

    @NotNull(message = "Order items are required")
    @NotEmpty(message = "Order items cannot be empty")
    List<OrderItemRequest> orderItems
) {
    
}
