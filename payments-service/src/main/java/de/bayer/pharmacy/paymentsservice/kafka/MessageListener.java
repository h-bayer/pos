package de.bayer.pharmacy.paymentsservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
  private final MessageHolder holder;
  public MessageListener(MessageHolder holder) { this.holder = holder; }

  @KafkaListener(topics = "#{'${spring.application.name}-topic'}")
  public void onMessage(String value) {
    holder.set(value);
  }
}
