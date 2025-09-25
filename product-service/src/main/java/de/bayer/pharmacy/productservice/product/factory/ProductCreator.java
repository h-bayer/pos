package de.bayer.pharmacy.productservice.product.factory;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;

import java.util.Map;

public interface ProductCreator {
    ProductType type();
    Product create(long sku, String name, String description, Map<String, Integer> initialAvailabilityByBranch);
}