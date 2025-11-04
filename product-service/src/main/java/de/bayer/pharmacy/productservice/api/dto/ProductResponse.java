package de.bayer.pharmacy.productservice.api.dto;

import java.time.Instant;
import java.util.Set;

public record ProductResponse(
      
        String sku,
        String name,
        String description,
        Instant createdAt,
        String type,   // ProductType as String
        String status, // ProductStatus as String
        Set<String> imageUrls,
        Set<BranchAvailability> availabilities,
        String approvedBy,
        Instant approvedAt
) {
    // Nested DTO for availability
    public record BranchAvailability(
            String branchCode,
            int availableQuantity
    ) { }
}