package org.example.s3learning.pinata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class pinataconfig {




    public WebClient webClient(String jwt) {
        return WebClient.builder().baseUrl("https://api.pinata.cloud").defaultHeader("Authorization","Bearer "+ jwt).build();

    }
}
