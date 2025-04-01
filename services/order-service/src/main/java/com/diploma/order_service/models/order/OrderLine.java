package com.diploma.order_service.models.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("order_line")
public class OrderLine {

    @Id
    private Integer id;

    private Integer orderId;

    private Integer productId;

    private BigDecimal quantity;
}
