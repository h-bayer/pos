package de.bayer.pharmacy.edgeposapi.api;

import de.bayer.pharmacy.edgeposapi.domain.product.Product;
import de.bayer.pharmacy.edgeposapi.service.ProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;

    public ProductController(ProductService s) {
        this.svc = s;
    }

    @GetMapping
    public List<Product> search(@RequestParam(required = false) String q) {
        return svc.search(q);
    }

    @PostMapping
    public Product create(@Validated @RequestBody Product p) {
        return svc.save(p);
    }

    @GetMapping("/sku/{sku}")
    public Product bySku(@PathVariable String sku) {
        return svc.bySku(sku);
    }
}
