package com.diploma.notification_service.models;

import java.math.BigDecimal;

public record PaymentConfirmation(
    String orderRef,
    BigDecimal quantity,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String email
) {
}
