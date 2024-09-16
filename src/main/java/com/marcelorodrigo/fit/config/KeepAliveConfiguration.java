package com.marcelorodrigo.fit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class KeepAliveConfiguration {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${keep-alive}")
    private String url;

    @Scheduled(fixedRate = 32_456, initialDelay = 120_000)
    public void keepAliveAtRenderDotCom() {
        try {
            var response = restTemplate.getForEntity(url, String.class);
            System.out.println("Reloaded at " + Instant.now() + ": Status Code " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Error reloading at " + Instant.now() + ": " + e.getMessage());
        }
    }
}
