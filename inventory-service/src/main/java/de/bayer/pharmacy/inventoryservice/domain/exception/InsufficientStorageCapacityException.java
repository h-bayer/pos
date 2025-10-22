package de.bayer.pharmacy.inventoryservice.domain.exception;

import de.bayer.pharmacy.inventoryservice.domain.model.Product;

public class InsufficientStorageCapacityException extends RuntimeException {
    public InsufficientStorageCapacityException(Product product, int remaining) {
        super("Cannot store " + remaining + " units of product " + product.getName()
                + " — warehouse is full.");
    }
}
