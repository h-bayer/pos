package de.bayer.pharmacy.productservice.domain.product.factory.creators;

import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;
import org.springframework.stereotype.Component;

@Component
public class RxProductCreator extends AbstractProductCreator {
    @Override public ProductType type() { return ProductType.RX; }

    @Override
    public Product create(long sku, String name, String description) {
        return baseWithAvailability(sku, name, description, ProductType.RX);
    }
}