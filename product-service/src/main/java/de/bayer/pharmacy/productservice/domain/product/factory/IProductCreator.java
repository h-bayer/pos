package de.bayer.pharmacy.productservice.domain.product.factory;

import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;



public interface IProductCreator {
    ProductType type();
    Product create(long sku, String name, String description);
}