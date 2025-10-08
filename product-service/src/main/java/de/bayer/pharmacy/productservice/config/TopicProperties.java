package de.bayer.pharmacy.productservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.topics")
public record TopicProperties(String productEvents, String inventoryEvents) {}
