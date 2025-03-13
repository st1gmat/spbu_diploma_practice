package com.diploma.product_service.services;

import com.diploma.product_service.exceptions.ProductBuyException;
import com.diploma.product_service.models.*;
import com.diploma.product_service.repository.CategoryRepository;
import com.diploma.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    public Mono<Integer> createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
        return productRepository.save(product)
                .map(Product::getId);
    }

    public Mono<ProductResponse> findById(Integer id) {
        return productRepository.findById(id)
            .flatMap(product ->
                categoryRepository.findById(product.getCategoryId()) // загружаем категорию
                    .map(category -> mapper.toProductResponse(product, category.getName(), category.getDescription()))
            )
            .switchIfEmpty(Mono.error(new RuntimeException("Product not found with ID: " + id)));
    }

    public Flux<ProductResponse> findAll() {
        return productRepository.findAll()
            .flatMap(product ->
                categoryRepository.findById(product.getCategoryId())
                    .map(category -> mapper.toProductResponse(product, category.getName(), category.getDescription()))
            );
    }

    @Transactional
    public Flux<ProductBuyResponse> buyProducts(Flux<ProductBuyRequest> requestFlux) {
        return requestFlux
            .collectList()
            .flatMapMany(requestList -> {
                var productIds = requestList.stream()
                        .map(ProductBuyRequest::productId)
                        .toList();

                return productRepository.findAllByIdInOrderById(productIds)
                        .collectList()
                        .flatMapMany(storedProducts -> {
                            if (productIds.size() != storedProducts.size()) {
                                return Flux.error(new ProductBuyException("One or more products do not exist"));
                            }

                            var sortedRequest = requestList.stream()
                                    .sorted(Comparator.comparing(ProductBuyRequest::productId))
                                    .toList();

                            return Flux.fromIterable(storedProducts)
                                    .zipWith(Flux.fromIterable(sortedRequest))
                                    .flatMap(tuple -> {
                                        var product = tuple.getT1();
                                        var request = tuple.getT2();

                                        if (product.getCurrentQuantity() < request.quantity()) {
                                            return Flux.error(new ProductBuyException(
                                                    "Insufficient stock quantity for product ID: " + request.productId()));
                                        }

                                        product.setCurrentQuantity(product.getCurrentQuantity() - request.quantity());

                                        return productRepository.save(product)
                                                .map(updatedProduct -> mapper.toProductBuyResponse(updatedProduct, request.quantity()));
                                    });
                        });
            });
    }
}
