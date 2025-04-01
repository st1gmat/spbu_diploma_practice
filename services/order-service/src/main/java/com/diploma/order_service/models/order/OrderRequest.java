package com.diploma.order_service.models.order;

import java.math.BigDecimal;
import java.util.List;

import com.diploma.order_service.models.product.BuyRequest;

public record OrderRequest(

    Integer id,
    String reference,

    BigDecimal requestedAmount,

    PaymentMethod paymentMethod,

    String customerId,

    List<BuyRequest> products
) {
}
