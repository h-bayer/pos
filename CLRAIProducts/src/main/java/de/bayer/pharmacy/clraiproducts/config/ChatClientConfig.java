package de.bayer.pharmacy.clraiproducts.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

@Configuration
public class ChatClientConfig {

    private final Logger log = LoggerFactory.getLogger(ChatClientConfig.class);

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ToolCallbackProvider mcpToolProvider) {

        Arrays.stream(mcpToolProvider.getToolCallbacks())
                .forEach(toolCallback -> {
                    log.debug(toolCallback.getToolMetadata().toString());
                    }
                );

        return  builder
                .defaultSystem("Du bist ein erfahrener Pharmazeut und SEO Experte. Du hältst Dich streng an die Fakten und erfindest nichts.")
                .defaultToolCallbacks(mcpToolProvider)
                .defaultToolContext(Map.of("progressToken",
                        "token-" + new Random().nextInt(1_000_000)))
                .build();

    }

    private final ToolCallbackProvider provider;

    public ChatClientConfig(ToolCallbackProvider provider) {
        this.provider = provider;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady(ApplicationReadyEvent event) {

        var tools = provider.getToolCallbacks();

        System.out.println("=== Registered MCP Tools ===");
        for (var t : tools) {
            System.out.println("TOOL: " + t.getToolMetadata());
        }
    }
}
