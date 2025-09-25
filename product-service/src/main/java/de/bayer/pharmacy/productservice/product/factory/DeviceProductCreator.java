package de.bayer.pharmacy.productservice.product.factory;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeviceProductCreator extends AbstractProductCreator {
    @Override public ProductType type() { return ProductType.DEVICE; }

    @Override
    public Product create(long sku, String name, String description, Map<String, Integer> initialAvailabilityByBranch) {
        // Add DEVICE-specific initialization or validation here
        return baseWithAvailability(sku, name, description, ProductType.DEVICE, initialAvailabilityByBranch);
    }
}
