package com.sem.ecommerce.product.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInventory {
    private UUID id;
    private Product product;
    private int quantity;

    @Builder
    public ProductInventory(UUID id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }
}
