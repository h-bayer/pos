package de.bayer.pharmacy.common.outbox;

import jakarta.persistence.*;


import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxMessage {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String topic;

    @Column(name = "key_ref")
    private String keyRef;

    @Column(nullable = false)
    private String type;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Lob
    private String headers;  // optional, JSON of extra headers

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxMessageStatus status = OutboxMessageStatus.PENDING;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name = "next_attempt_at", nullable = false)
    private OffsetDateTime nextAttemptAt = OffsetDateTime.now();

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    // ----- Constructors -----

    public OutboxMessage() {}

    public OutboxMessage(String topic, String keyRef, String type, String payload) {
        this.topic = topic;
        this.keyRef = keyRef;
        this.type = type;
        this.payload = payload;
    }

    // ----- Business methods -----

    public void markInProgress() {
        this.status = OutboxMessageStatus.IN_PROGRESS;
    }

    public void markSent() {
        this.status = OutboxMessageStatus.SENT;
        this.publishedAt = OffsetDateTime.now();
        this.lastError = null;
    }

    public void markFailed(String error, int nextDelaySeconds) {
        this.status = OutboxMessageStatus.PENDING;
        this.attemptCount++;
        this.lastError = error;
        this.nextAttemptAt = OffsetDateTime.now().plusSeconds(nextDelaySeconds);
    }

    // ----- Getters & Setters -----

    public UUID getId() { return id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getKeyRef() { return keyRef; }
    public void setKeyRef(String keyRef) { this.keyRef = keyRef; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    public String getHeaders() { return headers; }
    public void setHeaders(String headers) { this.headers = headers; }

    public OutboxMessageStatus getStatus() { return status; }
    public void setStatus(OutboxMessageStatus status) { this.status = status; }

    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }

    public OffsetDateTime getNextAttemptAt() { return nextAttemptAt; }
    public void setNextAttemptAt(OffsetDateTime nextAttemptAt) { this.nextAttemptAt = nextAttemptAt; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(OffsetDateTime publishedAt) { this.publishedAt = publishedAt; }

    @Override
    public String toString() {
        return "OutboxMessage{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", keyRef='" + keyRef + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", attemptCount=" + attemptCount +
                ", nextAttemptAt=" + nextAttemptAt +
                '}';
    }
}

