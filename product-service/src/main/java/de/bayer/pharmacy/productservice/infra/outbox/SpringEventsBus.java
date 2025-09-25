package de.bayer.pharmacy.productservice.infra.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.bayer.pharmacy.productservice.infra.outbox.events.ProductApprovedIntegrationEvent;
import de.bayer.pharmacy.productservice.infra.outbox.events.ProductPublishedIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventsBus implements IMessageBus {
    private final ApplicationEventPublisher publisher;
    private final ObjectMapper mapper;

    public SpringEventsBus(ApplicationEventPublisher publisher, ObjectMapper mapper) {
        this.publisher = publisher;
        this.mapper = mapper;
    }

    @Override
    public void publish(String type, String payload) {
        try {
            // Minimal type switch for demo purposes
            IIntegrationEvent evt = switch (type) {
                case "ProductPublishedIntegrationEvent" -> mapper.readValue(payload, ProductPublishedIntegrationEvent.class);
                case "ProductApprovedIntegrationEvent" -> mapper.readValue(payload, ProductApprovedIntegrationEvent.class);
//                case "StockReservationFailedIntegrationEvent" -> mapper.readValue(payload, StockReservationFailedIntegrationEvent.class);
                default -> throw new IllegalArgumentException("Unknown integration type: " + type);
            };
            publisher.publishEvent(evt); // fan it back into Spring as an application event
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}