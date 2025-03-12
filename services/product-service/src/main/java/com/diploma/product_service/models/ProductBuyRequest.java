package com.diploma.product_service.models;


public record ProductBuyRequest(
        Integer productId,
        double quantity
) {
}