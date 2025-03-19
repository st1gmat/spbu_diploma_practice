package com.diploma.product_service.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductBuyRequest(
        @NotNull(message = "product is required")
        Integer productId,
        @Positive(message = "quantity is required")
        double requestedQuantity
) {
}