package de.bayer.pharmacy.productservice.domain.product.commands;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.productservice.domain.product.Product;

public record PublishProductCommand(
        String sku

) implements ICommand<Product> {

    public PublishProductCommand {

    }
}