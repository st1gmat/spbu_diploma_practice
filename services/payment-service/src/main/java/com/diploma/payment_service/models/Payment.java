package com.diploma.payment_service.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("payment")
public class Payment {
    
    @Id
    private Integer id;

    private BigDecimal amount;

    @Column("payment_method")
    private PaymentMethod paymentMethod;

    @Column("created_date")
    @CreatedDate
    private LocalDateTime createdDate;
    
    @Column("last_modified_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
