package com.diploma.order_service.models.payment;

import java.math.BigDecimal;

import com.diploma.order_service.models.customer.CustomerResponse;
import com.diploma.order_service.models.order.PaymentMethod;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Integer orderId,
    String orderRef,
    CustomerResponse customer
) {
    
}
