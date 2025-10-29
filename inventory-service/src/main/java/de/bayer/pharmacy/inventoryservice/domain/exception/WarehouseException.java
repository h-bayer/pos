package de.bayer.pharmacy.inventoryservice.domain.exception;

import de.bayer.pharmacy.common.domain.exception.DomainException;

public class WarehouseException  extends DomainException
    {
        public WarehouseException(String message) {
            super(message);
        }
    }
