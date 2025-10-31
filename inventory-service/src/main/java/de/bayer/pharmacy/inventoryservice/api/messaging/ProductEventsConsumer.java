package de.bayer.pharmacy.inventoryservice.api.messaging;


import com.example.avro.ProductPublished;
import de.bayer.pharmacy.common.commandhandling.CommandBus;
import de.bayer.pharmacy.common.kafka.ProcessedEvent;
import de.bayer.pharmacy.common.kafka.ProcessedEventRepository;
import de.bayer.pharmacy.inventoryservice.application.command.PublishProductCommand;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductException;
import de.bayer.pharmacy.inventoryservice.domain.exception.ProductNotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsConsumer {

    private final CommandBus commandBus;

    //TODO: CDC/Debezium

    private final ProcessedEventRepository processedEventRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductEventsConsumer.class);

    public ProductEventsConsumer(CommandBus commandBus, ProcessedEventRepository processed) {
        this.commandBus = commandBus;
        this.processedEventRepository = processed;
    }

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 60000),
            dltTopicSuffix = ".DLT",
            autoCreateTopics = "true"
    )


    private void handleProductPublished(ProductPublished event) {
        var cmd = new PublishProductCommand(event.getSku().toString());
        commandBus.dispatch(cmd);
    }

    @KafkaListener(topics = "${app.topics.productEvents}", groupId = "${spring.kafka.consumer.group-id}")
    public void onProductPublished(@Header("eventId") String eventId,
                                   ConsumerRecord<String, ProductPublished> rec) {

        if (processedEventRepository.existsByEventId(eventId)) {
            // Already processed → skip silently
            return;
        }

        ProductPublished evt = rec.value();

        log.debug("Received product published event: {}", evt);

        try {
            handleProductPublished(evt);
            processedEventRepository.save(new ProcessedEvent(eventId, rec.topic(), rec.partition(), rec.offset()));
        } catch (ProductNotFoundException | ProductException e) {
            log.warn("Failed to process product event {}: {}", eventId, e.getMessage());
            // optionally store as failed event for monitoring
        } catch (Exception e) {
            log.error("Unexpected error while processing product event {}: {}", eventId, e.getMessage(), e);
            // rethrow if you want Kafka to retry:
            // throw e;
        }


    }


}