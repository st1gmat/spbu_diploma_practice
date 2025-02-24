package com.diploma.product_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diploma.product_service.models.ProductBuyRequest;
import com.diploma.product_service.models.ProductBuyResponse;
import com.diploma.product_service.models.ProductRequest;
import com.diploma.product_service.models.ProductResponse;
import com.diploma.product_service.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ResponseEntity<Integer> createProduct( @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(service.createProduct(request));
    }

    @PostMapping("/buy")
    public ResponseEntity<List<ProductBuyResponse>> buyProducts( @RequestBody List<ProductBuyRequest> request) {
        return ResponseEntity.ok(service.buyProducts(request));
    }
    
    @GetMapping("/{product_id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable("product_id") Integer productId) {
        return ResponseEntity.ok(service.findById(productId));
    }
    
    @GetMapping()
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
}
