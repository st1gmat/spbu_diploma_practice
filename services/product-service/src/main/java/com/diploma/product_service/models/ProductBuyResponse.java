package com.diploma.product_service.models;

import java.math.BigDecimal;

public record ProductBuyResponse(
        Integer productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {
}