package com.diploma.notification_service.models;

import java.math.BigDecimal;

public record Product(
    Integer productId,
    String name,
    String description,
    BigDecimal price,
    double quantity
) {
    
}
