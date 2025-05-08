package com.diploma.order_service.models.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("customer_order")
public class Order {

    @Id
    private Integer id;

    private String reference;

    private BigDecimal totalAmount;

    private String paymentMethod; // enum может быть сохранён как String

    private String status;

    private String customerId;

    private Instant createdDate;

    private Instant lastModifiedDate;

    // orderLines убираем, загружается вручную при необходимости
}