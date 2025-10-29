package de.bayer.pharmacy.inventoryservice.api.web;

import de.bayer.pharmacy.common.commandhandling.CommandBus;
import de.bayer.pharmacy.inventoryservice.api.dto.CreateWarehouseRequest;
import de.bayer.pharmacy.inventoryservice.api.dto.CreateWarehouseResponse;
import de.bayer.pharmacy.inventoryservice.application.command.CreateWareHouseCommand;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping
    ResponseEntity<?> create(@RequestBody final CreateWarehouseRequest warehouseRequest) {
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
                    .body(new CreateWarehouseResponse(warehouseRequest.code())); // optional body


            //TODO: adapt exception catch to more specific exceptions
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
