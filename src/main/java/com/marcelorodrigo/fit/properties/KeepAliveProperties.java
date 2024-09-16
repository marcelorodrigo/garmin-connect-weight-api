package com.marcelorodrigo.fit.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keep-alive")
public record KeepAliveProperties(String url) {
}
