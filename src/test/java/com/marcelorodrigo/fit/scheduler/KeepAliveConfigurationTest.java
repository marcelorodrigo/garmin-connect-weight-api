package com.marcelorodrigo.fit.scheduler;

import com.marcelorodrigo.fit.properties.KeepAliveProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeepAliveConfigurationTest {

    @Mock
    private RestClient restClient;

    @Mock
    private KeepAliveProperties keepAliveProperties;

    @InjectMocks
    private KeepAliveConfiguration keepAliveConfiguration;

    @Test
    void keepAliveAtRenderDotComShouldHandleServerError() {
        // Arrange
        String keepAliveUrl = "http://localhost:8080/weight";
        when(keepAliveProperties.url()).thenReturn(keepAliveUrl);
        when(restClient.get()).thenThrow(new RestClientResponseException("Server error", 500, "Internal Server Error", null, null, null));

        // Act & Assert - should not throw exception, just log error
        assertThatNoException()
                .isThrownBy(keepAliveConfiguration::keepAliveAtRenderDotCom);
    }

    @Test
    void keepAliveAtRenderDotComShouldHandleConnectionError() {
        // Arrange
        String keepAliveUrl = "http://localhost:8080/weight";
        when(keepAliveProperties.url()).thenReturn(keepAliveUrl);
        when(restClient.get()).thenThrow(new RuntimeException("Connection timeout"));

        // Act & Assert - should not throw exception, just log error
        assertThatNoException()
                .isThrownBy(keepAliveConfiguration::keepAliveAtRenderDotCom);
    }
}
