package com.diploma.payment_service.models;

import java.math.BigDecimal;

public record PaymentNotificationRequest(
    String orderRef,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String customerEmail
) {

}
