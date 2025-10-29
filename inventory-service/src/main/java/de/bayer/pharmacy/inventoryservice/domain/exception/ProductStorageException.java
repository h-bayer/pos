package de.bayer.pharmacy.inventoryservice.domain.exception;

import de.bayer.pharmacy.common.domain.exception.DomainException;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;

public class ProductStorageException extends DomainException {
    public ProductStorageException( String reason) {
        super( reason );
    }
}
