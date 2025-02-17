package com.diploma.customer_service.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
    String id,
    @NotNull(message = "missing firstname in request")
    String firstName,
    @NotNull(message = "missing lastname in request")
    String lastName,
    @NotNull(message = "missing email in request")
    @Email(message = "validation of email failed. smth wrong")
    String email,
    Address address
) {

}
