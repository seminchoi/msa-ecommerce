package com.sem.ecommerce.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Product {
    private UUID id;
    private Category category;
    private String name;
    private String description;
    private long price;
    private Map<String, Object> additionalAttributes = new HashMap<>();

    @Builder
    public Product(UUID id, Category category, String name, String description, long price, Map<String, Object> additionalAttributes) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.additionalAttributes = additionalAttributes;
    }
}
