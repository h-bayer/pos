package de.bayer.pharmacy.inventoryservice.application.handler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.inventoryservice.application.command.StoreDeliveryPositionCommand;
import de.bayer.pharmacy.inventoryservice.application.command.StoreDeliveryPositionResult;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

@Component
public class StoreDeliveryPositionCommandHandler implements ICommandHandler<StoreDeliveryPositionCommand, StoreDeliveryPositionResult> {

    private final WarehouseRepository warehouseRepository;

    public StoreDeliveryPositionCommandHandler(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public StoreDeliveryPositionResult handle(StoreDeliveryPositionCommand command) {
            return null;
    }

    @Override
    public Class<StoreDeliveryPositionCommand> commandType() {
        return StoreDeliveryPositionCommand.class;
    }
}
