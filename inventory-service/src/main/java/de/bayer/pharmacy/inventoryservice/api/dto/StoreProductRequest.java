package de.bayer.pharmacy.inventoryservice.api.dto;

public record StoreProductRequest(
        String productSku,
        int quantity
) { }