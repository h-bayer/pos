package de.bayer.pharmacy.inventoryservice.domain.inventory;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "inventory_entries",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_product_location",
                columnNames = {"product_id", "storage_location_id"}
        ),
        indexes = {
                @Index(name = "idx_inventory_product", columnList = "product_id"),
                @Index(name = "idx_inventory_location", columnList = "storage_location_id")
        })
public class InventoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_inventory_product"))
    private Product product;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_location_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_inventory_location"))
    private StorageLocation storageLocation;


    @Column(nullable = true)
    private Integer quantity;

    public InventoryEntry() {
    }

    public InventoryEntry(Product product, StorageLocation location, Integer quantity) {
        this.product = product;
        this.storageLocation = location;
        this.quantity = quantity;
    }

    // equals/hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryEntry other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // Getter/Setter
    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}