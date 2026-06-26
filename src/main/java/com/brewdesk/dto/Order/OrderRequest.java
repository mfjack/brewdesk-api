package com.brewdesk.dto.order;

import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @NotBlank(message = "Customer name is required") String customerName) {

}
