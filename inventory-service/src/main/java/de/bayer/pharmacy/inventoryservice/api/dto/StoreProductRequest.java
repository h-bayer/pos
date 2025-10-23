package de.bayer.pharmacy.inventoryservice.api.dto;

public record StoreProductRequest(
        String warehouseCode,
        String productSku,
        String productName,
        int quantity
) { }