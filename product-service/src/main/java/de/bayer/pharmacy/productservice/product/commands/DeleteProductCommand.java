package de.bayer.pharmacy.productservice.product.commands;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductType;

import java.util.Map;
import java.util.Objects;

public record DeleteProductCommand(
        long sku

) implements ICommand<Product> {

    public DeleteProductCommand {
        if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");

    }
}