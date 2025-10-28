package de.bayer.pharmacy.inventoryservice.infrastructure.repository;

import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Optional<Product> findByName(String productName);

    public Optional<Product> findBySku(String productSku);

}
