package de.bayer.pharmacy.productservice.product.factory.creators;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RxProductCreator extends AbstractProductCreator {
    @Override public ProductType type() { return ProductType.RX; }

    @Override
    public Product create(long sku, String name, String description, Map<String, Integer> initialAvailabilityByBranch) {
        return baseWithAvailability(sku, name, description, ProductType.RX, initialAvailabilityByBranch);
    }
}