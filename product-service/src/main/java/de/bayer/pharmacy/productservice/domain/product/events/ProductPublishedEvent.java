package de.bayer.pharmacy.productservice.domain.product.events;

public record ProductPublishedEvent(long sku, String name, String desc, String type) {
}
