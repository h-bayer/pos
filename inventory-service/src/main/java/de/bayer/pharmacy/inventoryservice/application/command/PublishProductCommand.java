package de.bayer.pharmacy.inventoryservice.application.command;

import de.bayer.pharmacy.common.commandhandling.ICommand;

public class PublishProductCommand implements ICommand<Void> {
    private String sku;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public PublishProductCommand(String sku, String name) {
        this.sku = sku;
        this.name = name;
    }
}
