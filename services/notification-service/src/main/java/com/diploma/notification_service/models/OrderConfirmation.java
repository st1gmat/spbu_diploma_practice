package com.diploma.notification_service.models;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
    String orderReference,
    BigDecimal quantity,
    PaymentMethod paymentMethod,
    Customer customer,
    List<Product> products

) {
    
}
