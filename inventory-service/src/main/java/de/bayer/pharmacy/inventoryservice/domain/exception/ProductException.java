package de.bayer.pharmacy.inventoryservice.domain.exception;

import de.bayer.pharmacy.common.domain.exception.DomainException;

public class ProductException extends DomainException {

    public ProductException(String message) {
        super(message);
    }
}
