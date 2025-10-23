package de.bayer.pharmacy.inventoryservice.api.web;

import de.bayer.pharmacy.common.commandhandling.CommandBus;
import de.bayer.pharmacy.inventoryservice.api.dto.StoreProductRequest;
import de.bayer.pharmacy.inventoryservice.application.command.StoreDeliveryPositionCommand;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private CommandBus commandBus;

    public WarehouseController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping("/store")
    public ResponseEntity<?> storeDeliveryPosition(@RequestBody StoreProductRequest request) {
        var product = new Product(request.productSku(), request.productName());
        var command = new StoreDeliveryPositionCommand(request.warehouseCode(), product, request.quantity());

        try {
            commandBus.dispatch(command);
            return ResponseEntity.ok().body("✅ Product stored successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}