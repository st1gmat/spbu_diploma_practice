package com.diploma.order_service.models.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BuyRequest(
    @NotNull
    Integer productId,
    
    @Positive
    double requestedQuantity
) {

}
