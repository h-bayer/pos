package de.bayer.pharmacy.productservice.product.factory;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CosmeticProductCreator extends AbstractProductCreator {
    @Override public ProductType type() { return ProductType.COSMETIC; }

    @Override
    public Product create(long sku, String name, String description, Map<String, Integer> initialAvailabilityByBranch) {
        return baseWithAvailability(sku, name, description, ProductType.COSMETIC, initialAvailabilityByBranch);
    }
}