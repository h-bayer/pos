package de.bayer.pharmacy.productservice.infra.outbox;

import jakarta.persistence.*;


import java.time.Instant;

@Entity
@Table(name = "outbox")
public class OutboxMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    String topic;
    String keyRef; // productId
    String type; // ProductCreated/ProductDeleted/StockLevelChanged
    @Lob String payload; // JSON blob with fields needed to build Avro
    Instant createdAt = Instant.now();
    boolean published = false;
    String error;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKeyRef() {
        return keyRef;
    }

    public void setKeyRef(String keyRef) {
        this.keyRef = keyRef;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

