package com.diploma.order_service.models.product;

public record BuyRequest(
    Integer productId,
    
    double requestedQuantity
) {

}
