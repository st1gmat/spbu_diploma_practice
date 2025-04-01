package com.diploma.product_service.services;


import org.springframework.stereotype.Service;

import com.diploma.product_service.models.Category;
import com.diploma.product_service.models.Product;
import com.diploma.product_service.models.ProductBuyResponse;
import com.diploma.product_service.models.ProductRequest;
import com.diploma.product_service.models.ProductResponse;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .currentQuantity(request.currentQuantity())
                .price(request.price())
                .category(
                        Category.builder()
                                .id(request.categoryId())
                                .build()
                )
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCurrentQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }

    public ProductBuyResponse toProductBuyResponse(Product product, double quantity) {
        return new ProductBuyResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}