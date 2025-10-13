package de.bayer.pharmacy.common.outbox;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.transaction.Transactional;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;


@Component
@EnableScheduling
public abstract class AbstractOutboxRelay {
    private static final Logger log = LoggerFactory.getLogger(AbstractOutboxRelay.class);
    private final OutboxRepository outbox;
    private final KafkaTemplate<String, Object> kafka; // Object ok with Avro serializer
    private final ObjectMapper om = new ObjectMapper();


    public AbstractOutboxRelay(OutboxRepository outbox, KafkaTemplate<String, Object> kafka) {
        this.outbox = outbox;

        this.kafka = kafka;
    }

    protected abstract Object buildAvro(OutboxMessage outboxMessage, ObjectNode node);

    // Poll the outbox and publish (demo-friendly; for prod CDC/Debezium or tx-producer)
    @Scheduled(fixedDelayString = "${outbox.flush-interval-ms:500}")
    @Transactional
    public void flush() {
        var batch = outbox.claimBatch(200); // adjust

        if (batch.isEmpty()) return;

        for (OutboxMessage outboxMessage : batch) {
            try {

                var node = (ObjectNode) om.readTree(outboxMessage.getPayload().toString());

                var avro = buildAvro(outboxMessage,node);

                // Header
                var headers = new RecordHeaders();

                headers.add("eventId", outboxMessage.getId().toString().getBytes());
                headers.add("eventType", outboxMessage.getType().getBytes());
                headers.add("eventVersion", "1".getBytes());

                var record = new ProducerRecord<String, Object>(outboxMessage.getTopic(), null, outboxMessage.getKeyRef(), avro, headers);

                kafka.send(record)
                        .whenComplete((md, ex) -> {
                            if (ex != null) {
                                log.error("Kafka send failed topic={} key={} err={}", outboxMessage.getTopic(), outboxMessage.getKeyRef(), ex.toString(), ex);
                                // Backoff: 1m, 5m, 30m, 2h … (Exponentiell mit Cap)
                                int attempt = outboxMessage.getAttemptCount() + 1;
                                outboxMessage.setAttemptCount(attempt);
                                Duration next = backoff(attempt);
                                outboxMessage.setNextAttemptAt(OffsetDateTime.now().plus(next));
                                outboxMessage.setStatus(OutboxMessageStatus.PENDING); // zurück in pending
                                outboxMessage.setLastError(ex.getClass().getSimpleName() + ": " + ex.getMessage());

                                log.warn("Outbox send failed id={} type={} attempt={} nextTryIn={}s err={}",
                                        outboxMessage.getId(), outboxMessage.getType(), attempt, next.toSeconds(), ex.toString());
                            } else {
                                log.debug("Kafka sent topic={} partition={} offset={} key={}",
                                        md.getRecordMetadata().topic(), md.getRecordMetadata().partition(), md.getRecordMetadata().offset(), outboxMessage.getKeyRef());
                            }
                        })
                        .get(); // if you want to block for the ack

                outboxMessage.setStatus(OutboxMessageStatus.SENT);
                outboxMessage.setPublishedAt(OffsetDateTime.now());
                outboxMessage.setLastError(null);


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