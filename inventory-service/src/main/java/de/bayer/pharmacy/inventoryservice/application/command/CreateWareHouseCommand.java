package de.bayer.pharmacy.inventoryservice.application.command;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.inventoryservice.domain.model.Warehouse;

public class CreateWareHouseCommand implements ICommand<Warehouse> {
    String warehouseCode;
    String warehouseAddress;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public CreateWareHouseCommand setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
        return this;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public CreateWareHouseCommand setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
        return this;
    }
}
