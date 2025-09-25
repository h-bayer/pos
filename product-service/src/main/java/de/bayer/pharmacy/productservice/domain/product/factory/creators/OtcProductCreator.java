package de.bayer.pharmacy.productservice.domain.product.factory.creators;

import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;
import org.springframework.stereotype.Component;

@Component
public class OtcProductCreator extends AbstractProductCreator {
    @Override public ProductType type() { return ProductType.OTC; }

    @Override
    public Product create(long sku, String name, String description) {
        // Add OTC-specific defaults/policies here if needed
        return baseWithAvailability(sku, name, description, ProductType.OTC);
    }
}