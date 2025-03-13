package com.diploma.customer_service.models;

public record CustomerRequest(
    String id,
    String firstName,
    String lastName,
    String email,
    Address address
) {

}
