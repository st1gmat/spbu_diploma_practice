package com.diploma.customer_service.models;

public record CustomerResponse(
    String id,
    String firstName,
    String lastName,
    String email,
    Address address
) {

}
