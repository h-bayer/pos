package de.bayer.pharmacy.productservice.product.events;

import java.time.Instant;

public record ProductRevertedToDraftEvent(Long id, long sku, Instant now) {
}
