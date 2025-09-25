package de.bayer.pharmacy.productservice.product.factory;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;



public interface IProductCreator {
    ProductType type();
    Product create(long sku, String name, String description);
}