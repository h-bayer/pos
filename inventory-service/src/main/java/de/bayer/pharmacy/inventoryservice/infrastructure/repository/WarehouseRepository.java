package de.bayer.pharmacy.inventoryservice.infrastructure.repository;

import de.bayer.pharmacy.inventoryservice.domain.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}
