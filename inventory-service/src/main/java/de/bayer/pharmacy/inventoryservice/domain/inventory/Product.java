package de.bayer.pharmacy.inventoryservice.domain.inventory;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "products",
        indexes = {
                @Index(name = "idx_product_sku", columnList = "sku", unique = true),
                @Index(name = "idx_product_name", columnList = "name")
        })
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    private String sku;

    @Column(nullable = false, length = 255)
    private String name;


    // Bidirektionales n:m zu Warehouse
    @ManyToMany
    @JoinTable(
            name = "warehouse_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "warehouse_id"),
            uniqueConstraints = @UniqueConstraint(name = "uk_warehouse_product", columnNames = {"product_id", "warehouse_id"})
    )
    private Set<Warehouse> warehouses = new HashSet<>();

    // Einlagerungen in konkreten StorageLocations (Join-Entität)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryEntry> inventoryEntries = new HashSet<>();

    // --- Convenience-Methoden ---
    public void addWarehouse(Warehouse warehouse) {
        if (warehouses.add(warehouse)) {
            warehouse.getProducts().add(this);
        }
    }

    public void removeWarehouse(Warehouse warehouse) {
        if (warehouses.remove(warehouse)) {
            warehouse.getProducts().remove(this);
        }
    }

    public void placeIn(StorageLocation location, Integer quantity) {
        InventoryEntry entry = new InventoryEntry(this, location, quantity);
        if (inventoryEntries.add(entry)) {
            location.getInventoryEntries().add(entry);
            // Konsistenz: Produkt gehört auch zum Warehouse der Location
            addWarehouse(location.getWarehouse());
        }
    }

    public void removeFrom(StorageLocation location) {
        inventoryEntries.removeIf(e -> {
            boolean match = e.getStorageLocation().equals(location);
            if (match) {
                location.getInventoryEntries().remove(e);
                e.setProduct(null);
                e.setStorageLocation(null);
            }
            return match;
        });
    }

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
    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Warehouse> getWarehouses() {
        return warehouses;
    }

    public Set<InventoryEntry> getInventoryEntries() {
        return inventoryEntries;
    }
}
