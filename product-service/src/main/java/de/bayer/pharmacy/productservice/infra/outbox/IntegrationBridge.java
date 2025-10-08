package de.bayer.pharmacy.productservice.infra.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bayer.pharmacy.productservice.config.TopicProperties;
import de.bayer.pharmacy.productservice.domain.product.events.ProductPublishedEvent;
import de.bayer.pharmacy.productservice.domain.product.repository.ProductRepository;
import de.bayer.pharmacy.productservice.infra.outbox.events.ProductApprovedIntegrationEvent;

import de.bayer.pharmacy.productservice.infra.outbox.events.ProductPublishedIntegrationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class IntegrationBridge {

    private final OutboxRepository outboxRepository;
    private final ProductRepository productRepository;
    private final TopicProperties topics;

    private final ObjectMapper objectMapper;

    public IntegrationBridge(OutboxRepository outbox, ProductRepository productRepository, TopicProperties topics, ObjectMapper mapper) {
        this.outboxRepository = outbox;
        this.productRepository = productRepository;
        this.topics = topics;
        this.objectMapper = mapper;
    }

    // Map domain → integration and persist AFTER the aggregate transaction commits
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(ProductPublishedEvent e) {

        var product = productRepository.findBySku(e.sku())
                .orElseThrow(); // must be present in the same tx

        var ie = new ProductPublishedIntegrationEvent(
                e.sku(),
                product.getName(),
                product.getDescription(),
                product.getType().name()
        );

        try {
            OutboxMessage outboxMessage = new OutboxMessage();
            outboxMessage.setTopic(topics.productEvents());
            outboxMessage.setKeyRef(String.valueOf(ie.sku()));
            outboxMessage.setType("ProductPublished");
            outboxMessage.setPayload(objectMapper.writeValueAsString(ie));

            outboxRepository.save(outboxMessage);

        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Cannot serialize integration event", ex);
        }
    }



}

