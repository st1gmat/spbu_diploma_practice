package com.diploma.product_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diploma.product_service.models.ProductBuyRequest;
import com.diploma.product_service.models.ProductBuyResponse;
import com.diploma.product_service.models.ProductRequest;
import com.diploma.product_service.models.ProductResponse;
import com.diploma.product_service.services.ProductService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<Integer> createProduct(@RequestBody ProductRequest request) {
        return service.createProduct(request);
    }

    @PostMapping("/buy")
    public Flux<ProductBuyResponse> buyProducts(@RequestBody Flux<ProductBuyRequest> request) {
        return service.buyProducts(request);
    }

    @GetMapping("/{product_id}")
    public Mono<ProductResponse> findById(@PathVariable("product_id") Integer productId) {
        return service.findById(productId);
    }

    @GetMapping()
    public Flux<ProductResponse> findAll() {
        return service.findAll();
    }
}
