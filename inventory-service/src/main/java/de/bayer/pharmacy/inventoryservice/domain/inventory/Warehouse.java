package de.bayer.pharmacy.inventoryservice.domain.inventory;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "warehouses",
        indexes = {
                @Index(name = "idx_warehouse_code", columnList = "code", unique = true),
                @Index(name = "idx_warehouse_name", columnList = "name")
        })
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64, unique = true)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 512)
    private String address;

    // Rückseite des n:m zu Product
    @ManyToMany(mappedBy = "warehouses")
    private Set<Product> products = new HashSet<>();

    // 1:n Storage Locations
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("code ASC")
    private List<StorageLocation> storageLocations = new ArrayList<>();

    // --- Convenience ---
    public void addStorageLocation(StorageLocation loc) {
        storageLocations.add(loc);
        loc.setWarehouse(this);
    }

    public void removeStorageLocation(StorageLocation loc) {
        storageLocations.remove(loc);
        loc.setWarehouse(null);
    }

    // equals/hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return Objects.hashCode(id); }

    // Getter/Setter
    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Set<Product> getProducts() { return products; }
    public List<StorageLocation> getStorageLocations() { return storageLocations; }
}
