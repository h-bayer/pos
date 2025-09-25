package de.bayer.pharmacy.productservice.domain.product.factory.creators;

import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;
import org.springframework.stereotype.Component;

@Component
public class DeviceProductCreator extends AbstractProductCreator {
    @Override public ProductType type() { return ProductType.DEVICE; }

    @Override
    public Product create(long sku, String name, String description) {
        // Add DEVICE-specific initialization or validation here
        return baseWithAvailability(sku, name, description, ProductType.DEVICE);
    }
}
