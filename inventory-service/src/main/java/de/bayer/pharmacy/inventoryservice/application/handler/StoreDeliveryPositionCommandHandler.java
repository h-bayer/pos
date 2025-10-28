package de.bayer.pharmacy.inventoryservice.application.handler;

import de.bayer.pharmacy.common.commandhandling.ICommandHandler;
import de.bayer.pharmacy.inventoryservice.application.command.StoreDeliveryPositionCommand;
import de.bayer.pharmacy.inventoryservice.application.command.StoreDeliveryPositionResult;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import de.bayer.pharmacy.inventoryservice.domain.model.ProductStatus;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.ProductRepository;
import de.bayer.pharmacy.inventoryservice.infrastructure.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StoreDeliveryPositionCommandHandler implements ICommandHandler<StoreDeliveryPositionCommand, StoreDeliveryPositionResult> {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;

    public StoreDeliveryPositionCommandHandler(WarehouseRepository warehouseRepository, ProductRepository productRepository) {
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
    }

    protected boolean productReadyForStorage(Product product) {
        return product.getStatus()== ProductStatus.APPROVED;
    }

    @Override
    public StoreDeliveryPositionResult handle(StoreDeliveryPositionCommand command) {
            var warehouse =  this.warehouseRepository.findByCode(command.warehouseCode())
                    .orElseThrow(()-> new IllegalArgumentException("Warehouse not found"));

            var productToStore = this.productRepository.findBySku(command.product().getSku())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));


            var storeResult = warehouse.store(productToStore, command.quantity());


            return storeResult;
    }

    @Override
    public Class<StoreDeliveryPositionCommand> commandType() {
        return StoreDeliveryPositionCommand.class;
    }
}
