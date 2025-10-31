package de.bayer.pharmacy.inventoryservice.application.command;

import de.bayer.pharmacy.common.commandhandling.ICommand;

public class PublishProductCommand implements ICommand<Void> {
    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public PublishProductCommand(String sku) {
        this.sku = sku;
    }
}
