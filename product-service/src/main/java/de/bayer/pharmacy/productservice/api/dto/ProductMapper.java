package de.bayer.pharmacy.productservice.api.dto;

import de.bayer.pharmacy.productservice.product.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getVersion(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getType(),
                product.getStatus(),
                product.getImages(),
                product.getAvailabilities(),
                product.getApprovedBy(),
                product.getApprovedAt()
        );
    }
}