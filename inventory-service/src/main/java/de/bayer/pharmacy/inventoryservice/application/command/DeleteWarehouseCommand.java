package de.bayer.pharmacy.inventoryservice.application.command;

import de.bayer.pharmacy.common.commandhandling.ICommand;

public class DeleteWarehouseCommand implements ICommand<Void> {
    String warehouseCode;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public DeleteWarehouseCommand(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}
