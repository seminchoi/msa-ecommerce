package com.sem.ecommerce.infra.dto;

import java.util.UUID;

public record OrderItemDto(
        UUID productId,
        int quantity
) {
}
