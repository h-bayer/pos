package de.bayer.pharmacy.inventoryservice.application.handler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.inventoryservice.application.command.DeleteWarehouseCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.WarehouseException;
import de.bayer.pharmacy.inventoryservice.domain.exception.WarehouseNotFoundException;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteWarehouseCommandHandler implements ICommandHandler<DeleteWarehouseCommand, Void> {
    private final WarehouseRepository warehouseRepository;

    public DeleteWarehouseCommandHandler(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Void handle(DeleteWarehouseCommand command) {
        var warehouse = warehouseRepository.findByCode(command.getWarehouseCode()).orElseThrow(
                () -> new WarehouseNotFoundException(command.getWarehouseCode())
        );

        if(warehouse.canBeDeleted()) {

            warehouseRepository.delete(warehouse);
        }
        else{
            throw new WarehouseException("cannot be deleted");
        }
        return null;
    }

    @Override
    public Class<DeleteWarehouseCommand> commandType() {
        return DeleteWarehouseCommand.class;
    }
}
