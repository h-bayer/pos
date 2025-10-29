package de.bayer.pharmacy.inventoryservice.domain.exception;

import de.bayer.pharmacy.common.domain.exception.DomainException;

public class WarehouseNotFoundException extends DomainException
{
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
