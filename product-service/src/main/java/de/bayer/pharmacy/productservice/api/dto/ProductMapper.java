package de.bayer.pharmacy.productservice.api.dto;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductImage;

import java.util.stream.Collectors;


public class ProductMapper {

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getType().name(),
                product.getStatus().name(),
                product.getImages().stream().map(ProductImage::getImageUrl).collect(Collectors.toSet()),
                product.getAvailabilities().stream()
                        .map(a -> new ProductResponse.BranchAvailability(a.getBranchCode(), a.getAvailableQuantity()))
                        .collect(Collectors.toSet()),
                product.getApprovedBy(),
                product.getApprovedAt()
        );
    }
}