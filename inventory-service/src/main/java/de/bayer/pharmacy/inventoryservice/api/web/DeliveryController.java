package de.bayer.pharmacy.inventoryservice.api.web;

import de.bayer.pharmacy.common.commandhandling.CommandBus;
import de.bayer.pharmacy.inventoryservice.api.dto.StoreProductRequest;
import de.bayer.pharmacy.inventoryservice.application.command.StoreDeliveryPositionCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductStorageException;
import de.bayer.pharmacy.inventoryservice.domain.exception.WarehouseNotFoundException;
import de.bayer.pharmacy.inventoryservice.domain.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryController {

    private final CommandBus commandBus;

    public DeliveryController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping("/{warehouseCode}/store")
    public ResponseEntity<?> storeDeliveryPosition(@PathVariable String warehouseCode, @RequestBody StoreProductRequest request) {

        var product = new Product(request.productSku());

        var command = new StoreDeliveryPositionCommand(warehouseCode, product, request.quantity());

        try {
            commandBus.dispatch(command);
            return ResponseEntity.ok().body("✅ Product stored successfully");
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(ProductStorageException e)
        {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}