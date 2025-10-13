package de.bayer.pharmacy.productservice.domain.product.factory.creators;

import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;
import org.springframework.stereotype.Component;

@Component
public class CosmeticProductCreator extends AbstractProductCreator {
    @Override public ProductType type() { return ProductType.COSMETIC; }

    @Override
    public Product create(long sku, String name, String description) {
        return buildBase(sku, name, description, ProductType.COSMETIC);
    }
}