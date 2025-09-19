package de.bayer.pharmacy.edgeposapi.service;

import de.bayer.pharmacy.edgeposapi.domain.product.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> search(String q) {
        return (q == null || q.isBlank()) ? repo.findAll() : repo.findByNameContainingIgnoreCase(q);
    }

    public Product bySku(String sku) {
        return repo.findBySku(sku);
    }

    @Transactional
    public Product save(Product p) {
        return repo.save(p);
    }
}
