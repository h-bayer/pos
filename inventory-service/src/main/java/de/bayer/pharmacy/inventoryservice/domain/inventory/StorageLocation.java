package de.bayer.pharmacy.inventoryservice.domain.inventory;

import jakarta.persistence.*;
import java.util.*;

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

    // Produkte an dieser Location (über Join-Entität)
    @OneToMany(mappedBy = "storageLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryEntry> inventoryEntries = new HashSet<>();

    // optional: Kapazitätsangaben
    private Integer capacityUnits;

    // equals/hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageLocation other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return Objects.hashCode(id); }

    // Getter/Setter
    public Long getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
    public Set<InventoryEntry> getInventoryEntries() { return inventoryEntries; }
    public Integer getCapacityUnits() { return capacityUnits; }
    public void setCapacityUnits(Integer capacityUnits) { this.capacityUnits = capacityUnits; }
}
