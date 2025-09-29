package de.bayer.pharmacy.productservice.infra.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bayer.pharmacy.productservice.domain.product.events.ProductApprovedEvent;
import de.bayer.pharmacy.productservice.domain.product.events.ProductPublishedEvent;
import de.bayer.pharmacy.productservice.infra.outbox.events.ProductApprovedIntegrationEvent;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class IntegrationBridge {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper mapper;

    public IntegrationBridge(OutboxRepository outbox, ObjectMapper mapper) {
        this.outboxRepository = outbox;
        this.mapper = mapper;
    }

    // Map domain → integration and persist AFTER the aggregate transaction commits
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ProductPublishedEvent e) {
        System.out.println(e.toString());
        //var ie = new OrderPlacedIntegrationEvent(e.orderId(), e.customerId(), e.lines());
        //save(ie);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ProductApprovedEvent e) {
        System.out.println(e.toString());
        var ie = new ProductApprovedIntegrationEvent(e.sku(), "test","testdesc","RX");
        save(ie);
    }

//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void on(StockReserved e) {
//        var ie = new StockReservedIntegrationEvent(e.orderId());
//        save(ie);
//    }
//
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void on(StockReservationFailed e) {
//        var ie = new StockReservationFailedIntegrationEvent(e.orderId(), e.reason());
//        save(ie);
//    }

    private void save(IIntegrationEvent event) {
        try {
            String json = mapper.writeValueAsString(event);
            outboxRepository.save(new OutboxMessage(event.getClass().getSimpleName(), json));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize integration event", ex);
        }
    }
}

