package de.bayer.pharmacy.productservice.product.events;

import java.time.Instant;

public record ProductApprovedEvent( long sku, Instant approvedAt, String approvedBy) {
}
