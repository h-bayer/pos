package de.bayer.pharmacy.productservice.config;

import de.bayer.pharmacy.productservice.api.ProductMCPTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    private final Logger log = LoggerFactory.getLogger(McpServerConfig.class);

    @Bean
    public ToolCallbackProvider productToolsCallback(ProductMCPTools productTools) {

        log.debug("Creating tool callback provider");

        return MethodToolCallbackProvider.builder()
                .toolObjects(productTools)
                .build();
    }
}
