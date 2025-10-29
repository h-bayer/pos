package de.bayer.pharmacy.inventoryservice.application.command;

public record CreateWarehouseResult(
    String warehouseCode,
    boolean hasError,
    String errorMessage
){}
