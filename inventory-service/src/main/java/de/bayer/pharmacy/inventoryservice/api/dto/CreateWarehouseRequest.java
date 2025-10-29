package de.bayer.pharmacy.inventoryservice.api.dto;

public record CreateWarehouseRequest(
        String code, String address
) {
}
