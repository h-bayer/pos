package de.bayer.pharmacy.productservice.infra.outbox;

import com.example.avro.ProductPublished;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.bayer.pharmacy.productservice.domain.product.events.ProductPublishedEvent;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
@EnableScheduling
public class OutboxRelay {
    private final OutboxRepository outbox;
    private final KafkaTemplate<String, Object> kafka; // Object ok with Avro serializer
    private final ObjectMapper om = new ObjectMapper();


    public OutboxRelay(OutboxRepository outbox, KafkaTemplate<String, Object> kafka) {
        this.outbox = outbox;

        this.kafka = kafka;
    }

    // Poll the outbox and publish (demo-friendly; for prod consider CDC/Debezium or tx-producer)
    @Scheduled(fixedDelay = 500)
    @Transactional
    public void flush() {
        var batch = outbox.fetchBatch(PageRequest.of(0, 100));
        for (OutboxMessage m : batch) {
            try {
                ObjectNode n = (ObjectNode) om.readTree(m.getPayload());
                Object payload;
                switch (m.getType()) {
                    case "ProductPublished" -> payload = ProductPublished.newBuilder()
                            .setProductId(n.get("productId").asText())
                            .setSku(n.get("sku").asText())
                            .setName(n.get("name").asText())
                            .setEventVersion(n.path("eventVersion").asText("1")).build();
//                    case "ProductDeleted" -> payload = ProductDeleted.newBuilder()
//                            .setProductId(n.get("productId").asText())
//                            .setReason(n.path("reason").isMissingNode()? null : n.get("reason").asText())
//                            .setEventVersion(n.path("eventVersion").asText("1")).build();
                    default -> throw new IllegalArgumentException("Unknown type " + m.getType());
                }
                kafka.send(m.getTopic(), m.getKeyRef(), payload).get();
                m.setPublished(true); m.setError(null);
            } catch (Exception ex) {
                m.setError(ex.getMessage());
            }
        }
        if (!batch.isEmpty()) {
            outbox.saveAll(batch);
        }

    }
}