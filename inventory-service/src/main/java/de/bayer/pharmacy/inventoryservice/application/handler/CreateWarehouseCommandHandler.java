package de.bayer.pharmacy.inventoryservice.application.handler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.inventoryservice.application.command.CreateWareHouseCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.WarehouseException;
import de.bayer.pharmacy.inventoryservice.domain.model.Warehouse;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.WarehouseRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateWarehouseCommandHandler implements ICommandHandler<CreateWareHouseCommand, Warehouse> {
    private final WarehouseRepository warehouseRepository;

    public CreateWarehouseCommandHandler(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    //TODO: policies not checked in playground
    protected boolean canWarehouseBeCreated(Warehouse warehouse) {
        return true;
    }
    @Override
    @Transactional
    public Warehouse handle(CreateWareHouseCommand command) {
        var newWarehouse = new Warehouse(command.getWarehouseCode(), command.getWarehouseAddress() );

        if(canWarehouseBeCreated(newWarehouse)) {
            var savedWarehouse = this.warehouseRepository.save(newWarehouse);

            return savedWarehouse;
        }
        else
        {
            throw new WarehouseException("Warehouse not created");
        }


    }

    @Override
    public Class<CreateWareHouseCommand> commandType() {
        return CreateWareHouseCommand.class;
    }
}
