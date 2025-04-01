package com.diploma.payment_service.models;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Validated
public record Customer(
    String id,

    @NotNull
    String firstName,

    @NotNull
    String lastName,

    @NotNull
    @Email
    String email
) {

}
