package de.bayer.pharmacy.labellocalizationservice.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Greeting {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String serviceName;
  private String message;
  private Instant createdAt = Instant.now();

  public Greeting() { }
  public Greeting(String serviceName, String message) {
    this.serviceName = serviceName;
    this.message = message;
  }

  public Long getId() { return id; }
  public String getServiceName() { return serviceName; }
  public String getMessage() { return message; }
  public Instant getCreatedAt() { return createdAt; }

  public void setId(Long id) { this.id = id; }
  public void setServiceName(String s) { this.serviceName = s; }
  public void setMessage(String m) { this.message = m; }
  public void setCreatedAt(Instant t) { this.createdAt = t; }
}
