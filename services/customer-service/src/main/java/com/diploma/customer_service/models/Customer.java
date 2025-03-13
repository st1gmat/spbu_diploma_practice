package com.diploma.customer_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Customer {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private Address address;
}