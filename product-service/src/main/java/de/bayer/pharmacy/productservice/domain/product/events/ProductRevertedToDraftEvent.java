package de.bayer.pharmacy.productservice.domain.product.events;

import java.time.Instant;

public record ProductRevertedToDraftEvent( String sku, Instant now) {
}
