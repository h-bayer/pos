package de.bayer.pharmacy.productservice.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IProduct {


    List<Object> pullDomainEvents();



    String getName();

    String getDescription();

    void setDescription(String description);

    Instant getCreatedAt();

    ProductType getType();

    void setType(ProductType type);

    Set<ProductImage> getImages();

    long getSku();

    void setSku(long sku);

    Instant getApprovedAt();

    void setApprovedAt(Instant approvedAt);

    Set<ProductAvailability> getAvailabilities();

    void addImage(ProductImage image);

    long getVersion();

    void setVersion(long version);

    void setName(String name);

    void setCreatedAt(Instant createdAt);

    ProductStatus getStatus();

    void setStatus(ProductStatus status);

    String getApprovedBy();

    void setApprovedBy(String approvedBy);

    // ---- State transitions
    void approve(String approver);

    void revertToDraft();

    void rename(String newName);

    void addAvailability(ProductAvailability availability);

    public record RegisterProductCommand(
            @NotBlank String sku,
            @NotBlank String name,
            @NotNull ProductType type,
            Map<String, Integer> initialAvailabilityByBranch // optional
    ) {
    }

    public record ProductId(Long value) {
    }
}
