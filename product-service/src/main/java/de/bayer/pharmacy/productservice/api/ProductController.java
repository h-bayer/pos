package de.bayer.pharmacy.productservice.api;

import de.bayer.pharmacy.common.commandhandling.CommandBus;
import de.bayer.pharmacy.productservice.api.dto.ProductMapper;
import de.bayer.pharmacy.productservice.api.dto.ProductCreateRequest;
import de.bayer.pharmacy.productservice.api.dto.ProductRequest;
import de.bayer.pharmacy.productservice.api.dto.ProductResponse;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;
import de.bayer.pharmacy.productservice.domain.product.commands.ApproveProductCommand;
import de.bayer.pharmacy.productservice.domain.product.commands.CreateProductCommand;
import de.bayer.pharmacy.productservice.domain.product.commands.DeleteProductCommand;
import de.bayer.pharmacy.productservice.domain.product.commands.PublishProductCommand;
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
    public ResponseEntity delete(@Valid @RequestBody ProductRequest request) {
        var command = new DeleteProductCommand(
                request.sku()
        );

        bus.dispatch(command);


        return ResponseEntity.noContent().build(); // HTTP 204
    }

    // Approve a product
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveProduct(@Valid @RequestBody ProductRequest request) {

        var command = new ApproveProductCommand(request.sku());

        var sku = bus.dispatch(command);

        return ResponseEntity.ok("Product " + sku + " approved successfully");
    }

    // Publish a product
    @PostMapping("/{id}/publish")
    public ResponseEntity<String> publishProduct(@Valid @RequestBody ProductRequest request) {

        var command = new PublishProductCommand(request.sku());

        var sku = bus.dispatch(command);

//        boolean published = productService.publishProduct(id);
//        if (!published) {
//            return ResponseEntity.notFound().build(); // 404 if product not found
//        }
        return ResponseEntity.ok("Product " + sku + " published successfully");
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
        var command = new CreateProductCommand(
                request.sku(),
                request.name(),
                request.description(),
                ProductType.valueOf(request.type())
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