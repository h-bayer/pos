package de.bayer.pharmacy.inventoryservice.domain.model;

import jakarta.persistence.*;

import java.util.*;

//fancy stuff like types (e.g. cooling, bigpack, ...) omitted
@Entity
@Table(name = "storage_locations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_location_code_per_warehouse",
                columnNames = {"warehouse_id", "code"}
        ),
        indexes = @Index(name = "idx_location_code", columnList = "code"))
public class StorageLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // z. B. "A-01-03"
    @Column(nullable = false, length = 64)
    private String code;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_location_warehouse"))
    private Warehouse warehouse;


    @OneToMany(mappedBy = "storageLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryEntry> inventoryEntries = new HashSet<>();

    // optional: Kapazitätsangaben
    private Integer capacityUnits;

    public int getStoredQuantity(Product product)
    {
        return this.inventoryEntries.stream()
                .filter(inventoryEntry -> inventoryEntry.getProduct().equals(product))
                .mapToInt(InventoryEntry::getQuantity)
                .sum();
    }

    public void store(Product product, int quantity) {
        if(quantity > getFreeCapacity()) {
            throw new IllegalArgumentException("Quantity exceeds capacity");
        }

        this.inventoryEntries
                .stream()
                .filter(inventoryEntry -> inventoryEntry.getProduct().equals(product))
                .findFirst()
                .ifPresentOrElse(
                        e -> e.setQuantity(e.getQuantity() + quantity),
                        () -> this.inventoryEntries.add(new InventoryEntry(product,this, quantity))
                );
    }



    public boolean isEmpty() {
        return inventoryEntries.isEmpty();
    }

    public boolean hasProduct(Product product) {
        return inventoryEntries.stream().anyMatch(entry -> entry.getProduct().equals(product));
    }

    public int getFreeCapacity() {
        int usedCapacity = inventoryEntries.stream().mapToInt(InventoryEntry::getQuantity).sum();
        return capacityUnits - usedCapacity;
    }

    // equals/hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageLocation other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public StorageLocation setCode(String code) {
        this.code = code;
        return this;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public StorageLocation setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
        return this;
    }

    public Set<InventoryEntry> getInventoryEntries() {
        return inventoryEntries;
    }

    public Integer getCapacityUnits() {
        return capacityUnits;
    }

    public StorageLocation setCapacityUnits(Integer capacityUnits) {
        this.capacityUnits = capacityUnits;
        return this;
    }

    public StorageLocation(Integer capacityUnits) {
        this.capacityUnits = capacityUnits;
    }

    //for jpa
    protected StorageLocation() {}
}
