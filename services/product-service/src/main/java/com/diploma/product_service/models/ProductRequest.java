package com.diploma.product_service.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(

        Integer id,
        @NotNull(message = "name is required")
        String name,
        @NotNull(message = "description is required")
        String description,
        @Positive(message = "current quantity must be positive")
        double currentQuantity,
        @Positive(message = "price should be positive")
        BigDecimal price,
        @NotNull(message = "category is required")
        Integer categoryId
) {
}
