package de.bayer.pharmacy.productservice.domain.product.commands;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;

import java.util.Objects;

public record CreateProductCommand(
        String sku,
        String name,
        String description,
        ProductType type
) implements ICommand<Product> {

    public CreateProductCommand {

        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(type, "type");
    }
}