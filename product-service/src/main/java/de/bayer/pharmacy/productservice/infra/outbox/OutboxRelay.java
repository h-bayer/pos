package de.bayer.pharmacy.productservice.infra.outbox;

import com.example.avro.ProductPublished;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.bayer.pharmacy.productservice.domain.product.events.ProductPublishedEvent;
import jakarta.transaction.Transactional;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;


@Component
@EnableScheduling
public class OutboxRelay {
    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);
    private final OutboxRepository outbox;
    private final KafkaTemplate<String, Object> kafka; // Object ok with Avro serializer
    private final ObjectMapper om = new ObjectMapper();


    public OutboxRelay(OutboxRepository outbox, KafkaTemplate<String, Object> kafka) {
        this.outbox = outbox;

        this.kafka = kafka;
    }

    // Poll the outbox and publish (demo-friendly; for prod consider CDC/Debezium or tx-producer)
    @Scheduled(fixedDelayString = "${outbox.flush-interval-ms:500}")
    @Transactional
    public void flush() {
        var batch = outbox.claimBatch(200); // adjust

        if (batch.isEmpty()) return;

        for (OutboxMessage m : batch) {
            try {
                var node = (ObjectNode) om.readTree(m.getPayload().toString());
                Object avro = switch (m.getType()) {
                    case "ProductPublished" -> com.example.avro.ProductPublished.newBuilder()
                            .setSku(node.get("sku").asText())
                            .setName(node.get("name").asText())
                            .setEventVersion(node.path("eventVersion").asText("1"))
                            .build();
                    default -> throw new IllegalArgumentException("Unknown type " + m.getType());
                };

                // Header
                var headers = new RecordHeaders();
                headers.add("eventId", m.getId().toString().getBytes());
                headers.add("eventType", m.getType().getBytes());
                headers.add("eventVersion", "1".getBytes());

                var record = new ProducerRecord<String, Object>(m.getTopic(), null, m.getKeyRef(), avro, headers);

                kafka.send(record)
                        .whenComplete((md, ex) -> {
                            if (ex != null) {
                                log.error("Kafka send failed topic={} key={} err={}", m.getTopic(), m.getKeyRef(), ex.toString(), ex);
                                // Backoff: 1m, 5m, 30m, 2h … (Exponentiell mit Cap)
                                int attempt = m.getAttemptCount() + 1;
                                m.setAttemptCount(attempt);
                                Duration next = backoff(attempt);
                                m.setNextAttemptAt(OffsetDateTime.now().plus(next));
                                m.setStatus(OutboxMessageStatus.PENDING); // zurück in pending
                                m.setLastError(ex.getClass().getSimpleName() + ": " + ex.getMessage());

                                log.warn("Outbox send failed id={} type={} attempt={} nextTryIn={}s err={}",
                                        m.getId(), m.getType(), attempt, next.toSeconds(), ex.toString());
                            } else {
                                log.debug("Kafka sent topic={} partition={} offset={} key={}",
                                        md.getRecordMetadata().topic(), md.getRecordMetadata().partition(), md.getRecordMetadata().offset(), m.getKeyRef());
                            }
                        })
                        .get(); // if you want to block for the ack
                m.setStatus(OutboxMessageStatus.SENT);
                m.setPublishedAt(OffsetDateTime.now());
                m.setLastError(null);


            } catch (Exception ex) {
                log.error("Kafka send failed", ex);
            }
        }
        outbox.flush();
    }

    private Duration backoff(int attempt) {
        long[] secs = {60, 300, 1800, 7200, 21600}; // 1m,5m,30m,2h,6h
        return Duration.ofSeconds(attempt <= secs.length ? secs[attempt-1] : secs[secs.length-1]);
    }
}