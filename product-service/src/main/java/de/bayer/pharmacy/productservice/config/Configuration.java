package de.bayer.pharmacy.productservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public NewTopic productEventsTopic(@Value("${app.topics.productEvents}") String name) {
        return TopicBuilder.name(name)
                .partitions(6)
                .replicas(1)
                .compact()   // oder delete, retention, configs etc.
                .build();
    }
}
