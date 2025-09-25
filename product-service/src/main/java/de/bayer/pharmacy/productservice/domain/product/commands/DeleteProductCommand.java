package de.bayer.pharmacy.productservice.domain.product.commands;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.productservice.domain.product.Product;

public record DeleteProductCommand(
        long sku

) implements ICommand<Void> {

    public DeleteProductCommand {
        if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");

    }
}