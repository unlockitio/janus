package org.example.s3learning;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class pinataconfig {
    @Value("${jwt}")
    private String jwt;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl("https://api.pinata.cloud").defaultHeader("Authorization","Bearer "+ jwt).build();

    }
}
