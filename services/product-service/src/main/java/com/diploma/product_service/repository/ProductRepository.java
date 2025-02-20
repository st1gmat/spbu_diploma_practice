package com.diploma.product_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.diploma.product_service.models.Product;

@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Integer>{
    List<Product> findAllByIdInOrderById(List<Integer> ids);
}
