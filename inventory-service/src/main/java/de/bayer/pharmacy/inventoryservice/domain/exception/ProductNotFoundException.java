package de.bayer.pharmacy.inventoryservice.domain.exception;

import de.bayer.pharmacy.common.domain.exception.DomainException;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
