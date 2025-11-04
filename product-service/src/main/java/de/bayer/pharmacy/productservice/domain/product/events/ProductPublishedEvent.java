package de.bayer.pharmacy.productservice.domain.product.events;

public record ProductPublishedEvent(String sku, String name, String desc, String type) {
}
