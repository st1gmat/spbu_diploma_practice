package com.diploma.order_service.models.product;

import java.math.BigDecimal;

public record BuyResponse(
    Integer productId,
    String name,
    String description,
    BigDecimal price,
    double amount
) {
}
