package de.bayer.pharmacy.inventoryservice.application.command;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;

public record StoreDeliveryPositionCommand(String warehouseCode, Product product, int quantity) implements ICommand<StoreDeliveryPositionResult> {

    public StoreDeliveryPositionCommand {
        // if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");

    }
}