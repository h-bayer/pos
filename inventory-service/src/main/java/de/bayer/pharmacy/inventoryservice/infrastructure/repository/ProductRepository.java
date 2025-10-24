package de.bayer.pharmacy.inventoryservice.infrastructure.repository;

import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Optional<Product> findByProductName(String productName);

    public Optional<Product> findByProductSku(String productSku);

}
