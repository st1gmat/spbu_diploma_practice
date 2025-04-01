package com.diploma.payment_service.models;

import java.math.BigDecimal;

public record PaymentRequest(
    Integer id,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderRef,
    Customer customer
) {
    
}
