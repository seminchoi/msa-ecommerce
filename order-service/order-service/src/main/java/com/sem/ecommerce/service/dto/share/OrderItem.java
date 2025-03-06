package com.sem.ecommerce.service.dto.share;

import java.util.UUID;

public record OrderItem(
        UUID productId,
        int quantity
) {
}
