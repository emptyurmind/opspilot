package com.opspilot.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI opsPilotOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("OpsPilot API")
                        .version("0.1.0")
                        .description("Multi-Agent 智能运维排障系统 API"));
    }
}

