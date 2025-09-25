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
        ProductType type,
        Map<String, Integer> initialAvailabilityByBranch
) implements ICommand<Product> {

    public CreateProductCommand {
        if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(type, "type");

        // Optional map: allow null → treat as empty, and validate entries if present
        if (initialAvailabilityByBranch != null) {
            for (var e : initialAvailabilityByBranch.entrySet()) {
                var branch = Objects.requireNonNull(e.getKey(), "branchCode");
                var qty = Objects.requireNonNull(e.getValue(), "availableQuantity");
                if (qty < 0) throw new IllegalArgumentException("availableQuantity for branch " + branch + " must be >= 0");
            }
        }
    }
}