package org.example.s3learning.kubo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class kuboconfig {

    public WebClient kubowebclient(String nodeaddress) {
        return WebClient.builder().baseUrl(nodeaddress).build();
    }

}
