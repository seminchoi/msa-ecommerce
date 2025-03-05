package com.sem.ecommerce.product.domain;

import java.util.Map;
import java.util.UUID;

public class Product {
    private UUID id;
    private Category category;
    private String name;
    private String description;
    private long price;
    private Map<String, Object> additionalAttributes;
}
