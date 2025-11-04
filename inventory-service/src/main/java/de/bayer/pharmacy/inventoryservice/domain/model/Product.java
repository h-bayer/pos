package de.bayer.pharmacy.inventoryservice.domain.model;

import de.bayer.pharmacy.common.domain.AbstractDomainEntity;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "products",
        indexes = {
                @Index(name = "idx_product_sku", columnList = "sku", unique = true),
                @Index(name = "idx_product_name", columnList = "name")
        })
public class Product extends AbstractDomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    private String sku;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.CREATED;

    @Enumerated(EnumType.STRING)
    private ProductType type = ProductType.STANDARD;




    // --- equals/hashCode nur über ID (nach Persistenz) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Product other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // --- Getter/Setter ---


    public ProductStatus getStatus() {
        return status;
    }

    public Product setStatus(ProductStatus status) {
        this.status = status;
        return this;
    }

    public ProductType getType() {
        return type;
    }

    public Product setType(ProductType type) {
        this.type = type;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public Product setSku(String sku) {
        this.sku = sku;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }



    public void approve()
    {
        this.status = ProductStatus.APPROVED;
    }

    public Product(String sku)
    {
        this.sku = sku;
    }

    public Product(String sku, String name) {
        this.sku = sku;
        this.name = name;
    }

    public Product() {

    }



}
