package de.bayer.pharmacy.inventoryservice.infrastructure.repository;

import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
