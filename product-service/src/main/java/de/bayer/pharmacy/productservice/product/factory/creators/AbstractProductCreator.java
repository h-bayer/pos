package de.bayer.pharmacy.productservice.product.factory.creators;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductAvailability;
import de.bayer.pharmacy.productservice.product.ProductType;
import de.bayer.pharmacy.productservice.product.factory.IProductCreator;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractProductCreator implements IProductCreator {

    protected Product buildBase(long sku, String name, String description, ProductType type) {
        if (sku <= 0) throw new IllegalArgumentException("sku must be > 0");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(type, "type");

        // Your entity has a ctor: Product(ProductType type, Long id, String name, String description)
        var product = new Product(type, null, name, description);
        product.setSku(sku);
        return product;
    }

    protected void applyInitialAvailability(Product product, Map<String, Integer> initialAvailabilityByBranch) {
        if (initialAvailabilityByBranch == null || initialAvailabilityByBranch.isEmpty()) return;

        for (var e : initialAvailabilityByBranch.entrySet()) {
            var branch = Objects.requireNonNull(e.getKey(), "branchCode");
            var qty = Objects.requireNonNull(e.getValue(), "availableQuantity");
            if (qty < 0) throw new IllegalArgumentException("availableQuantity for branch " + branch + " must be >= 0");

            product.addAvailability(new ProductAvailability(branch, qty));
        }
    }

    /** Convenience for subclasses: build + apply availability in one go. */
    protected Product baseWithAvailability(long sku, String name, String description,
                                           ProductType type, Map<String, Integer> initialAvailabilityByBranch) {
        var p = buildBase(sku, name, description, type);
        applyInitialAvailability(p, initialAvailabilityByBranch);
        return p;
    }
}
