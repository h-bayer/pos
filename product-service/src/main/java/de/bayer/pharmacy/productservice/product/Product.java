package de.bayer.pharmacy.productservice.product;


import de.bayer.pharmacy.productservice.product.events.ProductApprovedEvent;
import de.bayer.pharmacy.productservice.product.events.ProductRevertedToDraftEvent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

import java.util.*;



@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

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

    private void record(Object event) {
        domainEvents.add(event);
    }

    public List<Object> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public long getSku() {
        return sku;
    }

    public void setSku(long sku) {
        this.sku = sku;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Set<ProductAvailability> getAvailabilities() {
        return availabilities;
    }

    public void addImage(ProductImage image) {
        this.images.removeIf(i->i.getImageUrl().equals(image.getImageUrl()));
        images.add(image);
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    private static void require(boolean cond, String msg) {
        if (!cond) throw new IllegalStateException(msg);
    }

    // ---- State transitions
    public void approve(String approver) {
        require(status == ProductStatus.DRAFT, "Only DRAFT can be approved");
        this.status = ProductStatus.APPROVED;
        this.approvedAt = Instant.now();
        this.approvedBy = approver;

        record(new ProductApprovedEvent(id, sku, approvedAt, approvedBy));
    }

    public void revertToDraft() {
        require(status == ProductStatus.APPROVED, "Only APPROVED can revert to DRAFT");
        this.status = ProductStatus.DRAFT;
        this.approvedAt = null;
        this.approvedBy = null;

        record(new ProductRevertedToDraftEvent(id, sku, Instant.now()));
    }

    public void rename(String newName) {
        require(status == ProductStatus.DRAFT, "Only DRAFT products can be edited");
        this.name = newName;
    }

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

    public Product(ProductType type, Long id, String name, String description) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.description = description;
        this.images = images;
        this.availabilities = availabilities;

        this.createdAt = Instant.now();
    }




    public record RegisterProductCommand(
            @NotBlank String sku,
            @NotBlank String name,
            @NotNull ProductType type,
            Map<String, Integer> initialAvailabilityByBranch // optional
    ) {}

    public record ProductId(Long value) {}



//    public static final class ProductRegisteredEvent {
//        public final Long productId;
//        public final String sku;
//        public ProductRegisteredEvent(Long productId, String sku) {
//            this.productId = productId; this.sku = sku;
//        }
//    }



}
