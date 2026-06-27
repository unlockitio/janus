package org.example.s3learning.kubo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class kuboconfig {
    @Bean
    public WebClient kubowebclient() {
        return WebClient.builder().baseUrl("http://localhost:5001").build();
    }

}
