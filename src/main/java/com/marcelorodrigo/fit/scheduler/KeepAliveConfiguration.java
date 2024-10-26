package com.marcelorodrigo.fit.scheduler;

import com.marcelorodrigo.fit.properties.KeepAliveProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeepAliveConfiguration {
    private final RestTemplate restTemplate;
    private final KeepAliveProperties keepAliveProperties;

    @Scheduled(cron = "0 */5 6-9 * * ?", zone = "Europe/Amsterdam")
    public void keepAliveAtRenderDotCom() {
        try {
            val response = restTemplate.getForEntity(keepAliveProperties.url(), String.class);
            log.info("Reloaded {} at {} status {}", keepAliveProperties.url(), Instant.now(), response.getStatusCode());
        } catch (Exception e) {
            log.error("Error reloading {} at {}. {}", keepAliveProperties.url(), Instant.now(), e.getMessage());
        }
    }
}
