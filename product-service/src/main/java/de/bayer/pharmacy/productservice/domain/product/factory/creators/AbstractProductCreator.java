package de.bayer.pharmacy.productservice.domain.product.factory.creators;

import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;
import de.bayer.pharmacy.productservice.domain.product.factory.IProductCreator;

import java.util.Objects;

public abstract class AbstractProductCreator implements IProductCreator {

    protected Product buildBase(long sku, String name, String description, ProductType type) {
        if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(type, "type");


        var product = new Product(type, sku, name, description);

        return product;
    }


}
