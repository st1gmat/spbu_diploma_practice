package com.diploma.product_service.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diploma.product_service.exceptions.ProductBuyException;
import com.diploma.product_service.models.ProductBuyRequest;
import com.diploma.product_service.models.ProductBuyResponse;
import com.diploma.product_service.models.ProductRequest;
import com.diploma.product_service.models.ProductResponse;
import com.diploma.product_service.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Integer createProduct(
            ProductRequest request
    ) {
        var product = mapper.toProduct(request);
        return repository.save(product).getId();
    }

    public ProductResponse findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + id));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ProductBuyException.class)
    public List<ProductBuyResponse> buyProducts(
            List<ProductBuyRequest> request
    ) {
        var productIds = request
                .stream()
                .map(ProductBuyRequest::productId)
                .toList();
        var storedProducts = repository.findAllByIdInOrderById(productIds);
        if (productIds.size() != storedProducts.size()) {
            throw new ProductBuyException("One or more products does not exist");
        }
        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductBuyRequest::productId))
                .toList();
        var boughtProducts = new ArrayList<ProductBuyResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);
            if (product.getCurrentQuantity() < productRequest.quantity()) {
                throw new ProductBuyException("Insufficient stock quantity for product with ID:: " + productRequest.productId());
            }
            var newAvailableQuantity = product.getCurrentQuantity() - productRequest.quantity();
            product.setCurrentQuantity(newAvailableQuantity);
            repository.save(product);
            boughtProducts.add(mapper.toProductBuyResponse(product, productRequest.quantity()));
        }
        return boughtProducts;
    }

}