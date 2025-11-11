package com.marcelorodrigo.fit.fit;

import com.garmin.fit.DateTime;
import com.marcelorodrigo.fit.domain.WeightMeasurement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateWeightScaleMesgTest {
    @Test
    void generate() {
        // Arrange
        final var timeCreated = new DateTime(1234519810L);
        final var weightMeasurement = WeightMeasurement.builder()
                .weight(70.5f)
                .percentFat(15.0f)
                .percentHydration(60.0f)
                .muscleMass(30.0f)
                .boneMass(5.0f)
                .build();

        // Act
        final var weightScaleMesg = new GenerateWeightScaleMesg().execute(timeCreated, weightMeasurement);

        // Assert
        assertThat(weightScaleMesg).isNotNull();
        assertThat(weightScaleMesg.getWeight()).isEqualTo(70.5f);
        assertThat(weightScaleMesg.getPercentFat()).isEqualTo(15.0f);
        assertThat(weightScaleMesg.getPercentHydration()).isEqualTo(60.0f);
        assertThat(weightScaleMesg.getMuscleMass()).isEqualTo(30.0f);
        assertThat(weightScaleMesg.getBoneMass()).isEqualTo(5.0f);
        assertThat(weightScaleMesg.getTimestamp().getTimestamp()).isEqualTo(timeCreated.getTimestamp());
    }
}