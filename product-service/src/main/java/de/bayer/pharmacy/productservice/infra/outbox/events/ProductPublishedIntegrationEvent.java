package de.bayer.pharmacy.productservice.infra.outbox.events;

import de.bayer.pharmacy.common.outbox.IIntegrationEvent;

public record ProductPublishedIntegrationEvent(String sku, String name, String desc, String type) implements IIntegrationEvent {

}
