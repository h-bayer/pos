package de.bayer.pharmacy.productservice.api;

import de.bayer.pharmacy.productservice.domain.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductMCPTools {
    private final Logger log= LoggerFactory.getLogger(ProductMCPTools.class);

    @Tool(
            name = "create-product",
            description = "Erstellt ein neues Produkt mit Name und Preis."
    )
    public Product createProduct(
            @ToolParam(description = "Name des Produkts") String name,
            @ToolParam(description = "Preis in EUR") BigDecimal price
    ) {
        log.debug("Create product: {}", name);
//        return productService.create(name, price);
        return null;
    }

    @Tool(
            name = "get-product-by-id",
            description = "Liest ein Produkt aus der Datenbank anhand der ID."
    )
    public Product getProductById(
            @ToolParam(description = "ID des Produkts") Long id
    ) {
        log.debug("Get product: {}", id);
        return null;
//        return productService.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Produkt nicht gefunden: " + id));
    }

    @Tool(
            name = "search-products",
            description = "Sucht Produkte anhand eines Suchbegriffs im Namen."
    )
    public List<Product> searchProducts(
            @ToolParam(description = "Suchbegriff im Produktnamen") String query
    ) {
        log.debug("Search products: {}", query);
        return null;
//        return productService.findByNameContaining(query);
    }

}
