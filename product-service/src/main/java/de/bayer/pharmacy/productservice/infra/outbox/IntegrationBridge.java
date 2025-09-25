package de.bayer.pharmacy.productservice.infra.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class IntegrationBridge {

    private final OutboxRepository outbox;
    private final ObjectMapper mapper;

    public IntegrationBridge(OutboxRepository outbox, ObjectMapper mapper) {
        this.outbox = outbox;
        this.mapper = mapper;
    }

    // Map domain → integration and persist AFTER the aggregate transaction commits
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(OrderPlaced e) {
        var ie = new OrderPlacedIntegrationEvent(e.orderId(), e.customerId(), e.lines());
        save(ie);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(StockReserved e) {
        var ie = new StockReservedIntegrationEvent(e.orderId());
        save(ie);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(StockReservationFailed e) {
        var ie = new StockReservationFailedIntegrationEvent(e.orderId(), e.reason());
        save(ie);
    }

    private void save(IntegrationEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            outbox.save(new OutboxMessage(event.getClass().getSimpleName(), json));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize integration event", ex);
        }
    }
}

