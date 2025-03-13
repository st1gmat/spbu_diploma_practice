package com.diploma.product_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "product") // R2DBC-аналог @Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private Integer id;

    private String name;
    
    private String description;

    @Column("available_quantity")
    private double currentQuantity;

    private BigDecimal price;

    @Column("category_id")
    private Integer categoryId; // Вместо @ManyToOne
}
