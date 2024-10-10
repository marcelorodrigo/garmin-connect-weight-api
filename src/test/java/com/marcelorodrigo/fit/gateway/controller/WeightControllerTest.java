package com.marcelorodrigo.fit.gateway.controller;

import com.marcelorodrigo.fit.domain.WeightMeasurement;
import com.marcelorodrigo.fit.fit.FitWeight;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeightController.class)
class WeightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Clock clock;

    @MockBean
    private FitWeight fitWeight;

    @Test
    void createShouldReturnOk(@TempDir Path tempDir) throws Exception {
        // Mock the clock to return a fixed instant
        val fixedInstant = Instant.parse("2023-10-01T00:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));

        // Create a temporary file
        val tempFile = Files.createTempFile(tempDir, "weight-", ".fit");
        Files.write(tempFile, "dummy content".getBytes());

        // Mock the fitWeight to return the path to the temporary file
        when(fitWeight.create(WeightMeasurement.builder().weight(100.1f).timestamp(fixedInstant).build())).thenReturn(tempFile);

        mockMvc.perform(post("/weight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"weight\": 100.1, \"timestamp\": \"2023-10-01T00:00:00Z\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=weight-2023-10-01T00:00:00Z.fit"));
    }
}