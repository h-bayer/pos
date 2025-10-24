package de.bayer.pharmacy.inventoryservice.domain.exception;

import de.bayer.pharmacy.inventoryservice.domain.model.Product;

public class ProductStorageException extends RuntimeException {
    public ProductStorageException(Product product, String reason) {
        super("Product " + product.getId()   + " cannot be stored (" + reason + ")");
    }
}
