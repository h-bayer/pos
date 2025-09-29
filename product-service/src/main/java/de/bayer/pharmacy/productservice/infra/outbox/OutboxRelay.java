package de.bayer.pharmacy.productservice.infra.outbox;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxRelay {
    private final OutboxRepository outbox;
    private final IMessageBus bus;

    public OutboxRelay(OutboxRepository outbox, IMessageBus bus) {
        this.outbox = outbox;
        this.bus = bus;
    }

    // Poll the outbox and publish (demo-friendly; for prod consider CDC/Debezium or tx-producer)
    @Scheduled(fixedDelay = 10500) //in ms
    @Transactional
    public void flush() {
        List<OutboxMessage> batch = outbox.fetchBatch(PageRequest.of(0, 100));
        for (OutboxMessage m : batch) {
            bus.publish(m.getType(), m.getPayload());
            m.markPublished();
        }
        outbox.saveAll(batch);
    }
}