package de.bayer.pharmacy.productservice.infra.outbox;

import jakarta.persistence.*;


import java.time.Instant;

@Entity
@Table(name = "outbox")
public class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Integration event type (class simple name)
    @Lob
    private String payload;

    private boolean published = false;
    private Instant createdAt = Instant.now();

    protected OutboxMessage() {}

    public OutboxMessage(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public String getPayload() { return payload; }
    public boolean isPublished() { return published; }
    public void markPublished() { this.published = true; }
}

