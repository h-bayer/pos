package de.bayer.pharmacy.productservice.product.commands;

import de.bayer.pharmacy.common.commandhandling.Command;

public record CreateProductCommand(
        long sku, String name, String description
) implements Command<Long> { }