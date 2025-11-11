package de.bayer.pharmacy.clraiproducts.mcpserver;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {

    @Tool(description = "Find a product by SKU code")
    public String findProduct(String sku) {
        return "Product with SKU " + sku + " found!";
    }

    @Tool(description = "List all products")
    public String listProducts() {
        return "Available products: SKU-001, SKU-002";
    }


}
