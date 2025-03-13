package com.diploma.product_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.diploma.product_service.models.Category;

public interface CategoryRepository extends R2dbcRepository<Category, Integer>{
    
}
