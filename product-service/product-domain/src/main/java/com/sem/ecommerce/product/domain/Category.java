package com.sem.ecommerce.product.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    private String UUID;
    private String name;
    private Category parent;
    private List<Category> children;

    @Builder
    public Category(String UUID, String name, Category parent, List<Category> children) {
        this.UUID = UUID;
        this.name = name;
        this.parent = parent;
        this.children = children;
    }
}
