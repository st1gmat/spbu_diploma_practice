package com.diploma.order_service.models.order;

import java.math.BigDecimal;
import java.util.List;

import com.diploma.order_service.models.product.BuyRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(

    Integer id,
    String reference,

    @Positive
    BigDecimal requestedAmount,

    @NotNull
    PaymentMethod paymentMethod,

    @NotNull
    @NotEmpty
    @NotBlank
    String customerId,

    @NotEmpty
    List<BuyRequest> products
) {
}
