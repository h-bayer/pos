package de.bayer.pharmacy.inventoryservice.domain.inventory.repository;

import de.bayer.pharmacy.inventoryservice.domain.inventory.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
