package com.sem.ecommerce.product.domain;

import java.util.List;

public class Category {
    private String UUID;
    private String name;
    private Category parent;
    private List<Category> children;
}
