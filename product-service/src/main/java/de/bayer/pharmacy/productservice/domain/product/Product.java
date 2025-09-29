package de.bayer.pharmacy.productservice.domain.product;


import de.bayer.pharmacy.productservice.domain.product.events.ProductApprovedEvent;
import de.bayer.pharmacy.productservice.domain.product.events.ProductDeletedEvent;
import de.bayer.pharmacy.productservice.domain.product.events.ProductPublishedEvent;
import de.bayer.pharmacy.productservice.domain.product.events.ProductRevertedToDraftEvent;
import jakarta.persistence.*;

import java.time.Instant;

import java.util.*;



@Entity
public class Product{

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

    private String publishedBy;

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    private Instant publishedAt;







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
        return Collections.unmodifiableSet(availabilities);
    }


    public void upsertImage(ProductImage image) {
        this.images.removeIf(i->i.getImageUrl().equals(image.getImageUrl()));
        images.add(image);
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

    // ---- State transitions

    public void approve(String approver) {
        //TODO: set status check
        //require(status == ProductStatus.DRAFT, "Only DRAFT can be approved");
        this.status = ProductStatus.APPROVED;
        this.approvedAt = Instant.now();
        this.approvedBy = approver;

        record(new ProductApprovedEvent( sku));
    }

    public void publish(String publisher) {
        require(status == ProductStatus.APPROVED, "Only APPROVED can be published");
        this.status = ProductStatus.PUBLISHED;
        this.publishedAt = Instant.now();
        this.publishedBy = publisher;

        record(new ProductPublishedEvent(this.sku, this.name, this.description, this.type.name()));
    }


    public void revertToDraft() {
        require(status == ProductStatus.APPROVED, "Only APPROVED can revert to DRAFT");
        this.status = ProductStatus.DRAFT;
        this.approvedAt = null;
        this.approvedBy = null;

        record(new ProductRevertedToDraftEvent( sku, Instant.now()));
    }


    public void rename(String newName) {
        require(status == ProductStatus.DRAFT, "Only DRAFT products can be edited");
        this.name = newName;
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
        this.createdAt = Instant.now();
    }

    private void require(boolean cond, String msg) {
        if (!cond) throw new IllegalStateException(msg);
    }

    void record(Object event) {
        domainEvents.add(event);
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public boolean canBeDeleted() {
        return !this.availabilities.stream().anyMatch(a -> a.getAvailableQuantity()>0);
    }

    public void prepareDeletion()
    {
        this.record(new ProductDeletedEvent(sku));
    }

    public List<Object> pullDomainEvents() {
        var copy = List.copyOf(domainEvents);
        domainEvents.clear();
        return copy;
    }

}
