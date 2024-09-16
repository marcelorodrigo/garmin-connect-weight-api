package com.marcelorodrigo.fit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {

    @Bean
    public ZoneId getDefaultZone() {
        return ZoneId.of("UTC");
    }

    @Bean
    public Clock getClock() {
        return Clock.system(getDefaultZone());
    }
}
