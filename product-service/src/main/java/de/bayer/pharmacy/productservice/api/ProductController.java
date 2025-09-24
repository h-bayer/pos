package de.bayer.pharmacy.productservice.api;

import de.bayer.pharmacy.productservice.product.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService svc;

    public ProductController(ProductService s) {
        this.svc = s;
    }

//    @GetMapping
//    public List<Product> search(@RequestParam(required = false) String q) {
//        return svc.search(q);
//    }
//
//    @PostMapping
//    public Product create(@Validated @RequestBody Product p) {
//        return svc.save(p);
//    }

//    @GetMapping("/sku/{sku}")
//    public Product bySku(@PathVariable String sku) {
//        return svc.bySku(sku);
//    }
}
