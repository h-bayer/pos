package de.bayer.pharmacy.clraiproducts.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    @Bean
    ChatClient chatClient(ChatModel chatModel, SyncMcpToolCallbackProvider toolCallbackProvider) {
        return ChatClient
                .builder(chatModel)
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
                .build();
    }
}
