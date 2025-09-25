package de.bayer.pharmacy.productservice.product.factory;


import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;

import java.util.Map;

public interface IProductFactory {
    Product create(long sku, String name, String description, ProductType type,
                   Map<String,Integer> initialAvailabilityByBranch);
}
