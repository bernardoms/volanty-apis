package com.bernardoms.cavapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
@ConfigurationProperties(prefix = "car.client")
@Configuration
@Getter
@Setter
public class CarClientConfig {
    private long timeout;
    private String endpoint;
    private String partner;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setReadTimeout(Duration.ofMillis(timeout))
                .setConnectTimeout(Duration.ofMillis(timeout)).build();
    }
}
