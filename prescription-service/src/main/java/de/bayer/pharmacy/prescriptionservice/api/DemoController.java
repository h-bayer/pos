package de.bayer.pharmacy.prescriptionservice.api;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import de.bayer.pharmacy.prescriptionservice.repo.GreetingRepository;
import de.bayer.pharmacy.prescriptionservice.model.Greeting;
import de.bayer.pharmacy.prescriptionservice.kafka.MessageHolder;

@RestController
@RequestMapping("/api")
public class DemoController {
  private final GreetingRepository repo;
  private final StringRedisTemplate redis;
  private final KafkaTemplate<String,String> kafka;
  private final MessageHolder holder;
  private final String serviceName = "prescription-service";
  private final String topic = "prescription-service-topic";

  public DemoController(GreetingRepository repo, StringRedisTemplate redis, KafkaTemplate<String,String> kafka,
                        MessageHolder holder) {
    this.repo = repo; this.redis = redis; this.kafka = kafka; this.holder = holder;
  }

  @GetMapping("/hello")
  public Map<String,String> hello() {
    return Map.of("service", serviceName, "message", "Hello, world!");
  }

  // DB demo
  @PostMapping("/db/greet")
  public Greeting save(@RequestParam(defaultValue = "Hi") String message) {
    return repo.save(new Greeting(serviceName, message));
  }
  @GetMapping("/db/all")
  public List<Greeting> all() { return repo.findAll(); }

  // Redis demo
  @PostMapping("/redis/set")
  public Map<String,String> set(@RequestParam String key, @RequestParam String value) {
    redis.opsForValue().set(key, value);
    return Map.of("key", key, "value", value);
  }
  @GetMapping("/redis/get")
  public Map<String,String> get(@RequestParam String key) {
    String v = redis.opsForValue().get(key);
    return Map.of("key", key, "value", v == null ? "" : v);
  }

  // Kafka demo
  @PostMapping("/kafka/publish")
  public Map<String,String> publish(@RequestParam String value) {
    kafka.send(topic, value);
    return Map.of("topic", topic, "published", value);
  }
  @GetMapping("/kafka/last")
  public Map<String,String> last() {
    return Map.of("topic", topic, "last", holder.get());
  }
}
