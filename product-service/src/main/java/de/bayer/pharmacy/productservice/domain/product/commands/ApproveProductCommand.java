package de.bayer.pharmacy.productservice.domain.product.commands;

import de.bayer.pharmacy.common.commandhandling.ICommand;
import de.bayer.pharmacy.productservice.domain.product.Product;
import de.bayer.pharmacy.productservice.domain.product.ProductType;

import java.util.Objects;

public record ApproveProductCommand(
        long sku

) implements ICommand<Product> {

    public ApproveProductCommand {
        if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");

    }
}