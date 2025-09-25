package de.bayer.pharmacy.productservice.api;

import de.bayer.pharmacy.common.commandhandling.CommandBus;
import de.bayer.pharmacy.productservice.api.dto.ProductMapper;
import de.bayer.pharmacy.productservice.api.dto.ProductCreateRequest;
import de.bayer.pharmacy.productservice.api.dto.ProductRequest;
import de.bayer.pharmacy.productservice.api.dto.ProductResponse;
import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;
import de.bayer.pharmacy.productservice.product.commands.CreateProductCommand;
import de.bayer.pharmacy.productservice.product.commands.DeleteProductCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CommandBus bus; // using your CQRS setup

    public ProductController(CommandBus bus) {
        this.bus = bus;
    }

    @DeleteMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        var command = new DeleteProductCommand(
                request.sku()
        );

        Product product = bus.dispatch(command);


        return ResponseEntity.ok(ProductMapper.toResponse(product));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
        var command = new CreateProductCommand(
                request.sku(),
                request.name(),
                request.description(),
                ProductType.valueOf(request.type()),
                request.initialAvailabilityByBranch()
        );

        Product product = bus.dispatch(command);


        return ResponseEntity.ok(ProductMapper.toResponse(product));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
//
//        return ResponseEntity.ok(ProductMapper.toResponse(product));
//    }
}