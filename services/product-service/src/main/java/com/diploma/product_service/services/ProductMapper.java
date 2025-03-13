package com.diploma.product_service.services;

import org.springframework.stereotype.Service;
import com.diploma.product_service.models.Product;
import com.diploma.product_service.models.ProductBuyResponse;
import com.diploma.product_service.models.ProductRequest;
import com.diploma.product_service.models.ProductResponse;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        return Product.builder()
                // .id(request.id())
                .name(request.name())
                .description(request.description())
                .currentQuantity(request.currentQuantity())
                .price(request.price())
                .categoryId(request.categoryId()) // Теперь храним только categoryId
                .build();
    }

    public ProductResponse toProductResponse(Product product, String categoryName, String categoryDescription) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCurrentQuantity(),
                product.getPrice(),
                product.getCategoryId(),
                categoryName,
                categoryDescription
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
