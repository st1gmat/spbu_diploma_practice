package com.diploma.product_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.diploma.product_service.models.Product;
import reactor.core.publisher.Flux;
import java.util.List;

public interface ProductRepository extends R2dbcRepository<Product, Integer> {
    Flux<Product> findAllByIdInOrderById(List<Integer> ids);
}
