package de.bayer.pharmacy.productservice.product;


import de.bayer.pharmacy.productservice.product.events.ProductApprovedEvent;
import de.bayer.pharmacy.productservice.product.events.ProductRevertedToDraftEvent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

import java.util.*;



@Entity
public class Product implements IProduct {




    @Version
    private long version;

    @Id
    @Column(nullable = false)
    private long sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.DRAFT;

    @ElementCollection
    @CollectionTable(name = "product_image", joinColumns = @JoinColumn(name = "product_id"))
    private Set<ProductImage> images = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "product_availability", joinColumns = @JoinColumn(name = "product_id"))
    private Set<ProductAvailability> availabilities = new HashSet<>();


    private String approvedBy;

    private Instant approvedAt;

    @Transient private final List<Object> domainEvents = new ArrayList<>();

    @Override
    public List<Object> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }



    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public ProductType getType() {
        return type;
    }

    @Override
    public void setType(ProductType type) {
        this.type = type;
    }

    @Override
    public Set<ProductImage> getImages() {
        return images;
    }

    @Override
    public long getSku() {
        return sku;
    }

    @Override
    public void setSku(long sku) {
        this.sku = sku;
    }

    @Override
    public Instant getApprovedAt() {
        return approvedAt;
    }

    @Override
    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    @Override
    public Set<ProductAvailability> getAvailabilities() {
        return availabilities;
    }

    @Override
    public void addImage(ProductImage image) {
        this.images.removeIf(i->i.getImageUrl().equals(image.getImageUrl()));
        images.add(image);
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public ProductStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    @Override
    public String getApprovedBy() {
        return approvedBy;
    }

    @Override
    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    // ---- State transitions
    @Override
    public void approve(String approver) {
        require(status == ProductStatus.DRAFT, "Only DRAFT can be approved");
        this.status = ProductStatus.APPROVED;
        this.approvedAt = Instant.now();
        this.approvedBy = approver;

        record(new ProductApprovedEvent( sku, approvedAt, approvedBy));
    }

    @Override
    public void revertToDraft() {
        require(status == ProductStatus.APPROVED, "Only APPROVED can revert to DRAFT");
        this.status = ProductStatus.DRAFT;
        this.approvedAt = null;
        this.approvedBy = null;

        record(new ProductRevertedToDraftEvent( sku, Instant.now()));
    }

    @Override
    public void rename(String newName) {
        require(status == ProductStatus.DRAFT, "Only DRAFT products can be edited");
        this.name = newName;
    }

    @Override
    public void addAvailability(ProductAvailability availability) {
        if (availability.getAvailableQuantity() < 0) throw new IllegalArgumentException("quantity must be >= 0");

        Objects.requireNonNull(availability.getBranchCode(), "branchCode");

        availabilities.removeIf(a-> a.getBranchCode().equals(availability.getBranchCode()));

        availabilities.add(availability);
    }

    //JPA needs empty constructor
    public Product()
    {

    }

    public Product(ProductType type, Long sku, String name, String description) {
        this.type = type;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.images = images;
        this.availabilities = availabilities;

        this.createdAt = Instant.now();
    }

    private void require(boolean cond, String msg) {
        if (!cond) throw new IllegalStateException(msg);
    }

    void record(Object event) {
        domainEvents.add(event);
    }


    //    public static final class ProductRegisteredEvent {
//        public final Long productId;
//        public final String sku;
//        public ProductRegisteredEvent(Long productId, String sku) {
//            this.productId = productId; this.sku = sku;
//        }
//    }



}
