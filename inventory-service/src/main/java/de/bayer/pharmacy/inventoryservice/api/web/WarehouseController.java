package de.bayer.pharmacy.inventoryservice.api.web;

import de.bayer.pharmacy.common.commandhandling.CommandBus;
import de.bayer.pharmacy.inventoryservice.api.dto.warehouseRequest;
import de.bayer.pharmacy.inventoryservice.api.dto.warehouseResponse;
import de.bayer.pharmacy.inventoryservice.application.command.CreateWareHouseCommand;

import de.bayer.pharmacy.inventoryservice.application.command.DeleteWarehouseCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.WarehouseException;
import de.bayer.pharmacy.inventoryservice.domain.exception.WarehouseNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

//TODO: could be own service. Omitted in this case
@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final CommandBus commandBus;

    public WarehouseController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @DeleteMapping("/{warehouseCode}")
    public ResponseEntity<?> deleteWarehouse(@PathVariable String warehouseCode)
    {
        try
        {
            var deleteWarehouseCommand = new DeleteWarehouseCommand(warehouseCode);

            commandBus.dispatch(deleteWarehouseCommand);

            return ResponseEntity.ok().build();
        }
        catch (WarehouseNotFoundException ex)
        {
            return ResponseEntity.notFound().build();
        }
        catch(WarehouseException ex)
        {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody final warehouseRequest warehouseRequest) {
        var createWarehouseCommand = new CreateWareHouseCommand()
                .setWarehouseCode(warehouseRequest.code())
                .setWarehouseAddress(warehouseRequest.address());

        try {
            var warehouse = commandBus.dispatch(createWarehouseCommand);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{code}")
                    .buildAndExpand(warehouse.getCode())
                    .toUri();

            return ResponseEntity
                    .created(location)     // sets HTTP 201 + Location header
                    .body(new warehouseResponse(warehouseRequest.code())); // optional body


            //TODO: adapt exception catch to more specific exceptions
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
