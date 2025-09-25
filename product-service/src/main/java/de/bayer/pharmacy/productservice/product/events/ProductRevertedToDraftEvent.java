package de.bayer.pharmacy.productservice.product.events;

import java.time.Instant;

public record ProductRevertedToDraftEvent( long sku, Instant now) {
}
