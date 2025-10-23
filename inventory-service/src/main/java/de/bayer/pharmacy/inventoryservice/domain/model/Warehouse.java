package de.bayer.pharmacy.inventoryservice.domain.model;

import de.bayer.pharmacy.inventoryservice.domain.exception.InsufficientStorageCapacityException;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;


// fancy stuff like splitting when storing etc omitted
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

//    // Rückseite des n:m zu Product
//    @ManyToMany(mappedBy = "warehouses")
//    private Set<Product> products = new HashSet<>();

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


    public void store(Product product, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be greater than zero");

        int remaining = quantity;

        //fill free space in storage locations with the same product batch ignored for simplicity
        for (StorageLocation storageLocation : this.getStorageLocations().stream()
                .filter(l -> l.hasProduct(product))
                .filter(l -> l.getFreeCapacity() > 0)
                .toList()) {
            int freeCapacity = storageLocation.getFreeCapacity();

            if (remaining <= freeCapacity) {
                storageLocation.store(product, remaining);
                remaining = 0;
                break;
            } else {
                storageLocation.store(product, freeCapacity);
                remaining -= freeCapacity;
            }
        }

        if (remaining > 0) {
            for (StorageLocation storageLocation : this.getStorageLocations()
                    .stream()
                    .filter(l -> l.isEmpty())
                    .filter(l -> l.getFreeCapacity() > 0)
                    .toList()
            ) {

                int free = storageLocation.getFreeCapacity();

                if (remaining <= free) {
                    storageLocation.store(product, remaining);
                    remaining = 0;
                    break;
                } else {
                    storageLocation.store(product, free);
                    remaining -= free;
                }

                if (remaining > 0) {
                    throw new InsufficientStorageCapacityException(product, remaining);
                }
            }
        }
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
                        new IllegalStateException("product " + product.getSku() + " is not available in location " + locationCode));

        int newQty = entry.getQuantity() - quantity;
        if (newQty < 0)
            throw new IllegalStateException("insufficient stocklevel in storage location " + locationCode);

        if (newQty == 0) {

            location.getInventoryEntries().remove(entry);
            //product.getInventoryEntries().remove(entry);
        } else {
            entry.setQuantity(newQty);
        }
    }

    public Map<Product, Integer> getStoredProductsWithQuantity() {
        return this.storageLocations
                .stream()
                .flatMap(l -> l.getInventoryEntries().stream())
                .collect(Collectors.toMap(
                        InventoryEntry::getProduct,
                        InventoryEntry::getQuantity,
                        Integer::sum
                ));
    }

    public Map<StorageLocation, Map<Product, Integer>> getQuantitiesPerLocation() {
        return storageLocations.stream()
                .collect(Collectors.toMap(
                        loc -> loc,
                        loc -> loc.getInventoryEntries().stream()
                                .collect(Collectors.toMap(
                                        InventoryEntry::getProduct,
                                        InventoryEntry::getQuantity
                                ))
                ));
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

    public List<StorageLocation> getStorageLocations() {
        return storageLocations;
    }
}
