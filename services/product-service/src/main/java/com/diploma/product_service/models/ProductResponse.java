package com.diploma.product_service.models;

import java.math.BigDecimal;

public record ProductResponse(
    Integer id,
    String name,
    String description,
    double currentQuantity,
    BigDecimal price,
    Integer categoryId,
    String categoryName,
    String categoryDescription
) {

}
