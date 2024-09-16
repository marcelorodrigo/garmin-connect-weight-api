package com.marcelorodrigo.fit.fit;

import com.garmin.fit.DateTime;
import com.marcelorodrigo.fit.domain.WeightMeasurement;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class FitWeight {
    private final GenerateFileIdMesg generateFileIdMesg;
    private final GenerateWeightScaleMesg generateWeightScaleMesg;
    private final FitFileCreator fitFileCreator;

    public Path create(final Instant instant, final WeightMeasurement weightMeasurement) throws IOException {
        val timeCreated = new DateTime(Date.from(instant));
        val fileIdMesg = generateFileIdMesg.execute(timeCreated);
        val weightScale = generateWeightScaleMesg.execute(timeCreated, weightMeasurement);
        return fitFileCreator.execute(fileIdMesg, weightScale);
    }


}
