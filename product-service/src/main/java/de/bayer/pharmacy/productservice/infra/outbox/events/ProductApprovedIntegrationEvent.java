package de.bayer.pharmacy.productservice.infra.outbox.events;

import de.bayer.pharmacy.productservice.infra.outbox.IIntegrationEvent;

public record ProductApprovedIntegrationEvent(long sku, String name, String desc, String type) implements IIntegrationEvent {

}
