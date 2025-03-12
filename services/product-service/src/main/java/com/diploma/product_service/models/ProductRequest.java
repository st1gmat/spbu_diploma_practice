package com.diploma.product_service.models;

import java.math.BigDecimal;

public record ProductRequest(

        Integer id,
        String name,
        String description,
        double currentQuantity,
        BigDecimal price,
        Integer categoryId
) {
}
