package de.bayer.pharmacy.productservice.infra.outbox.relay;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.bayer.pharmacy.common.outbox.AbstractOutboxRelay;
import de.bayer.pharmacy.common.outbox.OutboxMessage;
import de.bayer.pharmacy.common.outbox.OutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OutboxRelay extends AbstractOutboxRelay {


    public OutboxRelay(OutboxRepository outbox, KafkaTemplate<String, Object> kafka) {
        super(outbox, kafka);
    }

    @Override
    protected Object buildAvro(OutboxMessage outboxMessage, ObjectNode node) {
        Object avro = switch (outboxMessage.getType()) {
                    case "ProductPublished" -> com.example.avro.ProductPublished.newBuilder()
                            .setSku(node.get("sku").asText())
                            .setName(node.get("name").asText())
                            .setEventVersion(node.path("eventVersion").asText("1"))
                            .build();
                    default -> throw new IllegalArgumentException("Unknown type " + outboxMessage.getType());
                };

        return avro;
    }
}
