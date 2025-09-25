package de.bayer.pharmacy.productservice.product.commands;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;

import java.util.Map;
import java.util.Objects;

public record CreateProductCommand(
        long sku,
        String name,
        String description,
        ProductType type
) implements ICommand<Product> {

    public CreateProductCommand {
        if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(type, "type");
    }
}