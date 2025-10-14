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


    public void addStorageLocation(StorageLocation loc) {
        storageLocations.add(loc);
        loc.setWarehouse(this);
    }

    public void removeStorageLocation(StorageLocation loc) {
        storageLocations.remove(loc);
        loc.setWarehouse(null);
    }

    private StorageLocation findStorageLocation(String code) {
        return storageLocations.stream()
                .filter(loc -> loc.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    public void store(Product product, String locationCode, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be greater than zero");
        StorageLocation location = findStorageLocation(locationCode);
        if (location == null)
            throw new IllegalArgumentException("Storagelocation " + locationCode + " does not exist in warehouse " + name);


        InventoryEntry entry = location.getInventoryEntries().stream()
                .filter(e -> e.getProduct().equals(product))
                .findFirst()
                .orElseGet(() -> {
                    InventoryEntry newEntry = new InventoryEntry(product, location, 0);
                    location.getInventoryEntries().add(newEntry);
                    product.getInventoryEntries().add(newEntry);
                    return newEntry;
                });

        entry.setQuantity(entry.getQuantity() + quantity);
    }


    public void outsource(Product product, String locationCode, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        StorageLocation location = findStorageLocation(locationCode);
        if (location == null)
            throw new IllegalArgumentException("storage location " + locationCode + " does not exist in warehouse " + name);

        InventoryEntry entry = location.getInventoryEntries().stream()
                .filter(e -> e.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("product " + product.getSku() + " is not available in location " + locationCode ));

        int newQty = entry.getQuantity() - quantity;
        if (newQty < 0)
            throw new IllegalStateException("insufficient stocklevel in storage location " + locationCode);

        if (newQty == 0) {

            location.getInventoryEntries().remove(entry);
            product.getInventoryEntries().remove(entry);
        } else {
            entry.setQuantity(newQty);
        }
    }


    // equals/hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse other)) return false;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public List<StorageLocation> getStorageLocations() {
        return storageLocations;
    }
}
