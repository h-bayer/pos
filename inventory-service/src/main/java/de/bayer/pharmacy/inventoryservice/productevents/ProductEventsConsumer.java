package de.bayer.pharmacy.inventoryservice.productevents;


import com.example.avro.ProductPublished;
import de.bayer.pharmacy.common.kafka.ProcessedEvent;
import de.bayer.pharmacy.common.kafka.ProcessedEventRepository;
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

    //TODO: CDC/Debezium

    private final ProcessedEventRepository processedEventRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductEventsConsumer.class);

    public ProductEventsConsumer(ProcessedEventRepository processed) {
        this.processedEventRepository = processed;
    }

    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 60000),
            dltTopicSuffix = ".DLT",
            autoCreateTopics = "true"
    )
    @KafkaListener(topics = "${app.topics.productEvents}", groupId = "${spring.kafka.consumer.group-id}")
    public void onProductPublished(@Header("eventId") String eventId,
                                   ConsumerRecord<String, ProductPublished> rec) {
        try {

            if (processedEventRepository.existsByEventId(eventId)) {
                // Already processed → skip silently
                return;
            }

            ProductPublished evt = rec.value();

            log.debug("Received product published event: {}", evt);



            processedEventRepository.save(new ProcessedEvent(eventId, rec.topic(), rec.partition(), rec.offset()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }



}