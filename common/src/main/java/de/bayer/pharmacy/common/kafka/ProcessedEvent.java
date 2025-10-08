package de.bayer.pharmacy.common.kafka;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "processed_event",
        indexes = {
                @Index(name = "idx_processed_event_topic_partition_offset", columnList = "topic, partition, offset"),
                @Index(name = "idx_processed_event_processed_at", columnList = "processed_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_processed_event_event_id", columnNames = {"event_id"})
        }
)
public class ProcessedEvent {

    @Id
    @GeneratedValue
    private UUID id;

    /** The unique event ID from the producer (Avro header “eventId”) */
    @Column(name = "event_id", nullable = false, unique = true, updatable = false)
    private String eventId;

    /** Kafka topic name */
    @Column(nullable = false)
    private String topic;

    /** Kafka partition number */
    @Column(nullable = false)
    private int partition;

    /** Kafka offset within the partition */
    @Column(nullable = false)
    private long offset;

    /** When the event was processed by this service */
    @Column(name = "processed_at", nullable = false)
    private OffsetDateTime processedAt = OffsetDateTime.now();

    protected ProcessedEvent() {}

    public ProcessedEvent(String eventId, String topic, int partition, long offset) {
        this.eventId = eventId;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }

    // --- Getters ---

    public UUID getId() { return id; }

    public String getEventId() { return eventId; }

    public String getTopic() { return topic; }

    public int getPartition() { return partition; }

    public long getOffset() { return offset; }

    public OffsetDateTime getProcessedAt() { return processedAt; }

    // --- Helper ---

    @Override
    public String toString() {
        return "ProcessedEvent{" +
                "eventId='" + eventId + '\'' +
                ", topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                ", processedAt=" + processedAt +
                '}';
    }
}