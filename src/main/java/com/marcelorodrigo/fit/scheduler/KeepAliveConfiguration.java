package com.marcelorodrigo.fit.scheduler;

import com.marcelorodrigo.fit.properties.KeepAliveProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeepAliveConfiguration {
    private final RestClient restClient;
    private final KeepAliveProperties keepAliveProperties;

    @Scheduled(cron = "0 */5 6-9 * * ?", zone = "Europe/Amsterdam")
    public void keepAliveAtRenderDotCom() {
        try {
            restClient.get()
                    .uri(keepAliveProperties.url())
                    .retrieve()
                    .toBodilessEntity();
            log.info("Keep-alive request to {} successful at {}", keepAliveProperties.url(), Instant.now());
        } catch (Exception e) {
            log.error("Error sending keep-alive request to {} at {}. {}", keepAliveProperties.url(), Instant.now(), e.getMessage());
        }
    }
}
