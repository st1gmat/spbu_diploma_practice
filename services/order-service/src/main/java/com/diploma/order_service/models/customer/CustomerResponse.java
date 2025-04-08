package com.diploma.order_service.models.customer;

public record CustomerResponse(
    String id,
    String firstName,
    String lastName,
    String email
) {

}
