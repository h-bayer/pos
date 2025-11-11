package de.bayer.pharmacy.clraiproducts.config;

import de.bayer.pharmacy.clraiproducts.mcpserver.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;

public class MCPConfig {

    private static final Logger log = LoggerFactory.getLogger(MCPConfig.class);


    @Bean
    ToolCallbackProvider authorTools() {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(new ProductRepository())
                .build();
    }

    @Bean
    McpSyncClientCustomizer mcpSyncClientCustomizer() {
        return (name, mcpClientSpec) -> {
            mcpClientSpec.toolsChangeConsumer(tools -> {
                log.info("Detected tools changes.");
            });
        };
    }
}
