package io.haechi.henesis.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class HttpClientConfig {
    @Autowired
    HenesisApiProperties henesisApiProperties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .defaultHeader("X-Henesis-Secret", this.henesisApiProperties.getApiSecret())
                .defaultHeader("Authorization", this.henesisApiProperties.getAccessToken())
                .build();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(this.henesisApiProperties.getEnclaveBaseUrl()));
        return restTemplate;
    }
}
