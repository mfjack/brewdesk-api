package com.brewdesk.dto.order;

import com.brewdesk.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(
                @NotNull(message = "Status is required") OrderStatus status,
                String observation) {
}