package com.diploma.order_service.models.order;

import java.math.BigDecimal;
import java.util.List;

import com.diploma.order_service.models.customer.CustomerResponse;
import com.diploma.order_service.models.product.BuyResponse;

public record OrderConfirmation(
    String orderReference,
    BigDecimal quantity,
    PaymentMethod paymentMethod,
    CustomerResponse customer,
    List<BuyResponse> products

) {

}
