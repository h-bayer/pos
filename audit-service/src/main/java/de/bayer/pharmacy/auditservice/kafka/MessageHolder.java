package de.bayer.pharmacy.auditservice.kafka;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class MessageHolder {
  private final AtomicReference<String> last = new AtomicReference<>("");
  public void set(String v) { last.set(v); }
  public String get() { return last.get(); }
}
