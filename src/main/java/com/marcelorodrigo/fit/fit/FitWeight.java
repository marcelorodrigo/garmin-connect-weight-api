package com.marcelorodrigo.fit.fit;

import com.garmin.fit.DateTime;
import com.marcelorodrigo.fit.domain.WeightMeasurement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FitWeight {
    private final Clock clock;
    private final GenerateFileIdMesg generateFileIdMesg;
    private final GenerateWeightScaleMesg generateWeightScaleMesg;
    private final FitFileCreator fitFileCreator;

    public Path create(final WeightMeasurement weightMeasurement) throws IOException {
        final var timeCreated = new DateTime(Date.from(Optional.ofNullable(weightMeasurement.timestamp()).orElse(clock.instant())));
        final var fileIdMesg = generateFileIdMesg.execute(timeCreated);
        final var weightScale = generateWeightScaleMesg.execute(timeCreated, weightMeasurement);
        return fitFileCreator.execute(fileIdMesg, weightScale);
    }


}
