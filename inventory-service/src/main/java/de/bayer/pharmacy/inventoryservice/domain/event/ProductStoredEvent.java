package de.bayer.pharmacy.inventoryservice.domain.event;

import de.bayer.pharmacy.inventoryservice.domain.model.Product;

public record ProductStoredEvent(String warehouseCode, Product product, int quantity) { }