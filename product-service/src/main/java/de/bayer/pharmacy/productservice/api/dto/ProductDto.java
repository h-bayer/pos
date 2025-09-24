package de.bayer.pharmacy.productservice.api.dto;

import de.bayer.pharmacy.productservice.product.ProductAvailability;
import de.bayer.pharmacy.productservice.product.ProductImage;
import de.bayer.pharmacy.productservice.product.ProductStatus;
import de.bayer.pharmacy.productservice.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Set;

public record ProductDto(
        Long id,

        long version,

        long sku,

        @NotBlank
        String name,

        @NotBlank
        String description,

        Instant createdAt,

        @NotNull
        ProductType type,

        ProductStatus status,

        Set<ProductImage> images,

        Set<ProductAvailability> availabilities,

        String approvedBy,

        Instant approvedAt
) {}
