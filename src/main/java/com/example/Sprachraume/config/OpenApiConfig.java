package com.example.Sprachraume.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        System.out.println("=== OpenApiConfig loaded ===");

        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("https://sprachraume-production.up.railway.app/api")
                                .description("Production server")
                ));
    }
}