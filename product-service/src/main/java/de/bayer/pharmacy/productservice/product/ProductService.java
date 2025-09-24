package de.bayer.pharmacy.productservice.product;

import de.bayer.pharmacy.productservice.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ProductService(ProductRepository productRepository, ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.applicationEventPublisher = eventPublisher;
    }

    @Transactional
    public void registerProduct(Product product) {

    }
}
