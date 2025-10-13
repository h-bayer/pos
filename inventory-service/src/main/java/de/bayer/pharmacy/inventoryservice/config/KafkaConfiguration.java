package de.bayer.pharmacy.inventoryservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
public class KafkaConfiguration {
    @Bean
    public NewTopic productEventsTopic(@Value("${app.topics.productEvents}") String name) {
        return TopicBuilder.name(name)
                .partitions(6)
                .replicas(1)
                .compact()   // oder delete, retention, configs etc.
                .build();
    }

    @Bean
    DefaultErrorHandler skipOnSerDeErrors() {
        var h = new DefaultErrorHandler(new FixedBackOff(0L, 0L)); // no retry
        h.addNotRetryableExceptions(DeserializationException.class, SerializationException.class);
        return h;
    }
}
