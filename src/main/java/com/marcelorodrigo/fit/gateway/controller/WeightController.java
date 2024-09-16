package com.marcelorodrigo.fit.gateway.controller;

import com.marcelorodrigo.fit.fit.FitWeight;
import com.marcelorodrigo.fit.domain.WeightMeasurement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Clock;

@Tag(name = "Weight")
@RestController
@RequestMapping("/weight")
@RequiredArgsConstructor
public class WeightController {
    private final Clock clock;
    private final FitWeight fitWeight;

    @PostMapping
    @Operation(summary = "Creates a weight measurement")
    @ApiResponse(responseCode = "200", description = "Weight measurement generated", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<InputStreamResource> create(@RequestBody final WeightMeasurement weightMeasurement) throws IOException {
        val now = clock.instant();
        val path = fitWeight.create(now, weightMeasurement);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=weight-" + now.toString() + ".fit");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(path.toFile().length())
                .body(resource);
    }
}
