package com.diploma.order_service.models.product;

import java.util.List;

public record BuyRequestWrapper(
    List<BuyRequest> requests,
    Integer orderId,
    int attempt
) {
    public BuyRequestWrapper incrementAttempt() {
        return new BuyRequestWrapper(requests, orderId, attempt + 1);
    }
}